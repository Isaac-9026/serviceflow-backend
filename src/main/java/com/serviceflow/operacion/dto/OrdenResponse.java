package com.serviceflow.operacion.dto;

import com.serviceflow.identidad.dto.UsuarioResponse;
import com.serviceflow.operacion.entity.EstadoOrden;

import java.time.LocalDateTime;

public record OrdenResponse(
        Long id,
        Long solicitudId,
        Long cotizacionId,
        String descripcion,
        LocalDateTime fechaProgramada,
        EstadoOrden estado,
        UsuarioResponse tecnicoAsignado,
        LocalDateTime completadoEn,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
