package com.example.prestamoschn.pago.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagoRequest {

    @NotNull(message = "Ingrese un monto")
    private BigDecimal monto;

    private String observacion;
}

