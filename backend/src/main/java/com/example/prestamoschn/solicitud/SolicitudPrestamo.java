package com.example.prestamoschn.solicitud;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudPrestamo {

    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private String motivo;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;
    private BigDecimal tasaInteres;
    private BigDecimal montoTotalPagar;
    private LocalDateTime fechaResolucion;
    private String comentarioResolucion;
}
