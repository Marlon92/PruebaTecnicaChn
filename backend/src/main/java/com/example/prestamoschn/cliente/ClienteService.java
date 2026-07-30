package com.example.prestamoschn.cliente;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prestamoschn.cliente.dto.ClienteRequest;
import com.example.prestamoschn.cliente.dto.ClienteResponse;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.listar()
                .stream()
                .map(this::dtoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id) {
        return dtoResponse(buscarClienteOFallar(id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        
        validarDpi(request.getNumeroIdentificacion(), null);

        Cliente cliente = new Cliente();
        copiarDatos(request, cliente);

        Long id = clienteRepository.insertar(cliente);

        return dtoResponse(buscarClienteOFallar(id));
    }

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

    @Transactional
    public void eliminar(Long id) {
        buscarClienteOFallar(id);
        clienteRepository.eliminar(id);
    }

    private Cliente buscarClienteOFallar(Long id) {
        return clienteRepository.obtener(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un cliente con id " + id));
    }

    private void validarDpi(String dpi, Long idClienteActual) {
        clienteRepository.buscarPorDpi(dpi)
                .filter(existente -> !existente.getId().equals(idClienteActual))
                .ifPresent(existente -> {
                    throw new IllegalStateException("Ya existe un cliente con ese numero de identificacion");
                });
    }

    private void copiarDatos(ClienteRequest request, Cliente cliente) {
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setNumeroIdentificacion(request.getNumeroIdentificacion());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setDireccion(request.getDireccion());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
    }

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

