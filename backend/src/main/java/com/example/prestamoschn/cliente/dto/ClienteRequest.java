package com.example.prestamoschn.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteRequest {

    @NotBlank(message = "Ingrese un nombre")
    private String nombre;

    @NotBlank(message = "Ingrese Apellido")
    private String apellido;

    @NotBlank(message = "Ingrese DPI")
    private String numeroIdentificacion;

    @NotNull(message = "Ingrese fecha nacimiento")
    @Past(message = "La fecha deber ser anterior")
    private LocalDate fechaNacimiento;

    private String direccion;

    @Email(message = "Correo con formato incorrecto")
    private String correo;

    private String telefono;
}

