package com.example.prestamoschn.solicitud;

import com.example.prestamoschn.cliente.ClienteRepository;
import com.example.prestamoschn.pago.PagoRepository;
import com.example.prestamoschn.solicitud.dto.AprobarSolicitudRequest;
import com.example.prestamoschn.solicitud.dto.RechazarSolicitudRequest;
import com.example.prestamoschn.solicitud.dto.SolicitudRequest;
import com.example.prestamoschn.solicitud.dto.SolicitudResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final PagoRepository pagoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
                             ClienteRepository clienteRepository,
                             PagoRepository pagoRepository) 
    {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.pagoRepository = pagoRepository;
    }

    //devuelvo todas las solicitudes luego las parseo a mi DTO de respuesta
    @Transactional(readOnly = true)
    public List<SolicitudResponse> listar() {
        return solicitudRepository.listar().stream().map(this::aResponse).toList();
    }

    //obtengo las solicitudes por cliente
    @Transactional(readOnly = true)
    public List<SolicitudResponse> listarPorCliente(Long clienteId) {
        return solicitudRepository.listarPorCliente(clienteId).stream().map(this::aResponse).toList();
    }

    //obtengo la solicitud por id
    @Transactional(readOnly = true)
    public SolicitudResponse obtener(Long id) {
        return aResponse(buscarSolicitudOFallar(id));
    }

    //se almacena una nueva solicitud
    @Transactional
    public SolicitudResponse crear(SolicitudRequest request) {
        clienteRepository.obtener(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("No existe un cliente con id " + request.getClienteId()));

        SolicitudPrestamo solicitud = new SolicitudPrestamo();
        solicitud.setClienteId(request.getClienteId());
        solicitud.setMontoSolicitado(request.getMontoSolicitado());
        solicitud.setPlazoMeses(request.getPlazoMeses());
        solicitud.setMotivo(request.getMotivo());

        Long id = solicitudRepository.insertar(solicitud);

        return aResponse(buscarSolicitudOFallar(id));
    }

    //Aprobamos la solicitudes
    @Transactional
    public SolicitudResponse aprobar(Long id, AprobarSolicitudRequest request) {
        SolicitudPrestamo solicitud = buscarSolicitudOFallar(id);

        //calculo el interes, el monto total lo multiplico por la tasa, ejemplo 5% y lo divido entre 100 para obtener el interes
        BigDecimal interes = solicitud.getMontoSolicitado()
                .multiply(request.getTasaInteres())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        //como ya tengo el interes entonces al monto se lo añado.
        BigDecimal montoTotalPagar = solicitud.getMontoSolicitado().add(interes);

        solicitudRepository.aprobar(id, request.getTasaInteres(), montoTotalPagar, request.getComentario());

        return aResponse(buscarSolicitudOFallar(id));
    }

    @Transactional
    public SolicitudResponse rechazar(Long id, RechazarSolicitudRequest request) {
        
        solicitudRepository.rechazar(id, request.getComentario());

        return aResponse(buscarSolicitudOFallar(id));
    }

    private SolicitudPrestamo buscarSolicitudOFallar(Long id) {
        return solicitudRepository.obtener(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una solicitud con id " + id));
    }

    private SolicitudResponse aResponse(SolicitudPrestamo solicitud) {
        SolicitudResponse response = new SolicitudResponse();
        response.setId(solicitud.getId());
        response.setClienteId(solicitud.getClienteId());
        response.setClienteNombreCompleto(solicitud.getClienteNombre() + " " + solicitud.getClienteApellido());
        response.setMontoSolicitado(solicitud.getMontoSolicitado());
        response.setPlazoMeses(solicitud.getPlazoMeses());
        response.setMotivo(solicitud.getMotivo());
        response.setEstado(solicitud.getEstado());
        response.setFechaSolicitud(solicitud.getFechaSolicitud());
        response.setTasaInteres(solicitud.getTasaInteres());
        response.setMontoTotalPagar(solicitud.getMontoTotalPagar());
        response.setFechaResolucion(solicitud.getFechaResolucion());
        response.setComentarioResolucion(solicitud.getComentarioResolucion());

        if (solicitud.getEstado() == EstadoSolicitud.APROBADA) {
            BigDecimal totalPagado = pagoRepository.totalPagadoPorSolicitud(solicitud.getId());
            response.setTotalPagado(totalPagado);
            response.setSaldoPendiente(solicitud.getMontoTotalPagar().subtract(totalPagado));
        }

        return response;
    }
}
