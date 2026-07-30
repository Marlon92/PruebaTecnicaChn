package com.example.prestamoschn.pago.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PagoResponse {

    private Long id;
    private Long solicitudId;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String observacion;
}
