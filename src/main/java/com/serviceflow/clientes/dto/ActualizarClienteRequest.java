package com.serviceflow.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarClienteRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        String nombre,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 50, message = "El teléfono no puede exceder 50 caracteres")
        String telefono,

        @Email(message = "Debe ser un formato de email válido")
        @Size(max = 255, message = "El email no puede exceder 255 caracteres")
        String email,

        @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
        String direccion,
        
        String notas
) {}
