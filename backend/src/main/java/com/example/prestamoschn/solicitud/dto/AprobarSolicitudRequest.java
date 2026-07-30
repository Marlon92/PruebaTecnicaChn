package com.example.prestamoschn.solicitud.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AprobarSolicitudRequest {

    @NotNull(message = "La tasa de interes es obligatoria")
    @DecimalMin(value = "0.0", message = "La tasa de interes no puede ser negativa")
    private BigDecimal tasaInteres;

    private String comentario;
}

