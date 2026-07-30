package com.example.prestamoschn.cliente;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prestamoschn.cliente.dto.ClienteRequest;
import com.example.prestamoschn.cliente.dto.ClienteResponse;

import java.util.List;

@Service
public class ClienteService {

    //inyectamos dependencias
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //obtenemos todos los clientes
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.listar()
                .stream()
                .map(this::dtoResponse)
                .toList();
    }

    //obtenemos clientes por id
    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id) {
        return dtoResponse(buscarClienteOFallar(id));
    }

    //creamos un nuevo cliente
    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        
        validarDpi(request.getNumeroIdentificacion(), null);

        Cliente cliente = new Cliente();
        copiarDatos(request, cliente);

        Long id = clienteRepository.insertar(cliente);

        return dtoResponse(buscarClienteOFallar(id));
    }

    //Actualizamos clinte
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        buscarClienteOFallar(id);
        validarDpi(request.getNumeroIdentificacion(), id);

        Cliente cliente = new Cliente();
        cliente.setId(id);
        copiarDatos(request, cliente);
        clienteRepository.actualizar(cliente);

        return dtoResponse(buscarClienteOFallar(id));
    }

    //Eliminamos cliente
    @Transactional
    public void eliminar(Long id) {
        buscarClienteOFallar(id);
        clienteRepository.eliminar(id);
    }

    //valida que exista el cliente y sino da error
    private Cliente buscarClienteOFallar(Long id) {
        return clienteRepository.obtener(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un cliente con id " + id));
    }

    //valida si el dpi ya está en otro cliente
    private void validarDpi(String dpi, Long idClienteActual) {
        clienteRepository.buscarPorDpi(dpi)
                .filter(existente -> !existente.getId().equals(idClienteActual))
                .ifPresent(existente -> {
                    throw new IllegalStateException("Ya existe un cliente con ese numero de identificacion");
                });
    }

    //Parseamos lo del modelo a nuestro DTo de petición
    private void copiarDatos(ClienteRequest request, Cliente cliente) {
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setNumeroIdentificacion(request.getNumeroIdentificacion());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setDireccion(request.getDireccion());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
    }

    //Parseamos el model a nuestro DTO de respuesta
    private ClienteResponse dtoResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setApellido(cliente.getApellido());
        response.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        response.setFechaNacimiento(cliente.getFechaNacimiento());
        response.setDireccion(cliente.getDireccion());
        response.setCorreo(cliente.getCorreo());
        response.setTelefono(cliente.getTelefono());
        response.setFechaRegistro(cliente.getFechaRegistro());
        return response;
    }
}

