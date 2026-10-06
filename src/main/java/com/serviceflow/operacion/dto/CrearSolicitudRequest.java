package com.serviceflow.operacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(
        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        Long categoriaId,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
        String direccion
) {}
