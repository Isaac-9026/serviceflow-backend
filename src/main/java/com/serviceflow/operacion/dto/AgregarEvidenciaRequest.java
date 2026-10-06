package com.serviceflow.operacion.dto;

import com.serviceflow.operacion.entity.TipoEvidencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgregarEvidenciaRequest(
        @NotNull(message = "El tipo de evidencia es obligatorio")
        TipoEvidencia tipo,
        
        String rutaArchivo,
        
        @NotBlank(message = "La nota descriptiva es obligatoria")
        String nota
) {}
