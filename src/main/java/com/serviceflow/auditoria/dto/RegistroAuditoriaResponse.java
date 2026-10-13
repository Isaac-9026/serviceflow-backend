package com.serviceflow.auditoria.dto;

import java.time.LocalDateTime;

public record RegistroAuditoriaResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        String usuarioEmail,
        String accion,
        String tipoEntidad,
        Long entidadId,
        String detalles,
        LocalDateTime creadoEn
) {}
