package com.serviceflow.operacion.dto;

import com.serviceflow.operacion.entity.TipoEvidencia;

import java.time.LocalDateTime;

public record EvidenciaResponse(
        Long id,
        Long ordenId,
        TipoEvidencia tipo,
        String rutaArchivo,
        String nota,
        LocalDateTime creadoEn
) {}
