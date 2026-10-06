package com.serviceflow.operacion.dto;

import com.serviceflow.operacion.entity.EstadoOrden;

import java.time.LocalDateTime;

public record OrdenResponse(
        Long id,
        Long solicitudId,
        Long cotizacionId,
        String descripcion,
        LocalDateTime fechaProgramada,
        EstadoOrden estado,
        LocalDateTime completadoEn,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
