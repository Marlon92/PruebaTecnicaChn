package com.example.prestamoschn.solicitud;

import com.example.prestamoschn.solicitud.dto.AprobarSolicitudRequest;
import com.example.prestamoschn.solicitud.dto.RechazarSolicitudRequest;
import com.example.prestamoschn.solicitud.dto.SolicitudRequest;
import com.example.prestamoschn.solicitud.dto.SolicitudResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<SolicitudResponse> listar() {

        System.out.print("entre");
        return solicitudService.listar();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<SolicitudResponse> listarPorCliente(@PathVariable Long clienteId) {
        return solicitudService.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public SolicitudResponse obtener(@PathVariable Long id) {
        return solicitudService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest request) {
        SolicitudResponse creada = solicitudService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}/aprobar")
    public SolicitudResponse aprobar(@PathVariable Long id, @Valid @RequestBody AprobarSolicitudRequest request) {
        return solicitudService.aprobar(id, request);
    }

    @PutMapping("/{id}/rechazar")
    public SolicitudResponse rechazar(@PathVariable Long id, @RequestBody RechazarSolicitudRequest request) {
        return solicitudService.rechazar(id, request);
    }
}
