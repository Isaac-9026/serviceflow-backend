package com.serviceflow.operacion.dto;

import jakarta.validation.constraints.NotNull;

public record AsignarTecnicoRequest(
        @NotNull(message = "El técnico es obligatorio")
        Long tecnicoId,
        
        boolean esPrincipal
) {}
