package com.example.prestamoschn.pago;

import com.example.prestamoschn.pago.dto.PagoRequest;
import com.example.prestamoschn.pago.dto.PagoResponse;
import com.example.prestamoschn.solicitud.EstadoSolicitud;
import com.example.prestamoschn.solicitud.SolicitudPrestamo;
import com.example.prestamoschn.solicitud.SolicitudRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final SolicitudRepository solicitudRepository;

    public PagoService(PagoRepository pagoRepository, SolicitudRepository solicitudRepository) {
        this.pagoRepository = pagoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listar(Long solicitudId) {
        return pagoRepository.listarPorSolicitud(solicitudId)
                .stream()
                .map(this::aResponse)
                .toList();
    }

    @Transactional
    public PagoResponse registrar(Long solicitudId, PagoRequest request) {
        SolicitudPrestamo solicitud = solicitudRepository.obtener(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("No existe una solicitud con id " + solicitudId));

        if (solicitud.getEstado() != EstadoSolicitud.APROBADA) {
            throw new IllegalStateException("Solo se pueden registrar pagos de solicitudes aprobadas");
        }

        BigDecimal totalPagado = pagoRepository.totalPagadoPorSolicitud(solicitudId);
        BigDecimal saldoPendiente = solicitud.getMontoTotalPagar().subtract(totalPagado);

        if (request.getMonto().compareTo(saldoPendiente) > 0) {
            throw new IllegalStateException("El pago excede el saldo pendiente de " + saldoPendiente);
        }

        Pago pago = new Pago();
        pago.setSolicitudId(solicitudId);
        pago.setMonto(request.getMonto());
        pago.setObservacion(request.getObservacion());

        Long id = pagoRepository.insertar(pago);
        pago.setId(id);

        return aResponse(pago);
    }

    private PagoResponse aResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setId(pago.getId());
        response.setSolicitudId(pago.getSolicitudId());
        response.setMonto(pago.getMonto());
        response.setFechaPago(pago.getFechaPago());
        response.setObservacion(pago.getObservacion());
        return response;
    }
}

