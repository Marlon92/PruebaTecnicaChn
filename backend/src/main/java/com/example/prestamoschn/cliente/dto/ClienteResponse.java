package com.example.prestamoschn.cliente.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String correo;
    private String telefono;
    private LocalDateTime fechaRegistro;
}

