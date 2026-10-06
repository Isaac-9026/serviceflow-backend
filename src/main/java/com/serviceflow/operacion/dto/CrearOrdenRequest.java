package com.serviceflow.operacion.dto;

import jakarta.validation.constraints.NotNull;

public record CrearOrdenRequest(
        @NotNull(message = "La solicitud es obligatoria")
        Long solicitudId,

        Long cotizacionId,
        
        String descripcion
) {}
