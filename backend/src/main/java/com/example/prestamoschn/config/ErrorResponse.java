package com.example.prestamoschn.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private String mensaje;
    private Map<String, String> errores;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public ErrorResponse(String mensaje, Map<String, String> errores) {
        this.mensaje = mensaje;
        this.errores = errores;
    }
}
