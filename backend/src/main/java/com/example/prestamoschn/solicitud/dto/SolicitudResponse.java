package com.example.prestamoschn.solicitud.dto;

import com.example.prestamoschn.solicitud.EstadoSolicitud;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudResponse {

    private Long id;
    private Long clienteId;
    private String clienteNombreCompleto;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private String motivo;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;
    private BigDecimal tasaInteres;
    private BigDecimal montoTotalPagar;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;
    private LocalDateTime fechaResolucion;
    private String comentarioResolucion;
}
