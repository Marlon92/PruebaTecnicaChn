package com.example.prestamoschn.pago;

import com.example.prestamoschn.pago.dto.PagoRequest;
import com.example.prestamoschn.pago.dto.PagoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/solicitudes/{solicitudId}/pagos")
public class PagoController {

    //inyecto mi servicio de pago
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    //obtengo el id de solicitud y consulto
    @GetMapping
    public List<PagoResponse> listar(@PathVariable Long solicitudId) {
        return pagoService.listar(solicitudId);
    }

    //método para registrar un nuevo pago
    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@PathVariable Long solicitudId, @Valid @RequestBody PagoRequest request) {
        PagoResponse creado = pagoService.registrar(solicitudId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado); //devuelvo el estado CREADO 201
    }
}

