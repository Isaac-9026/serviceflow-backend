package com.serviceflow.operacion.dto;

import com.serviceflow.operacion.entity.EstadoSolicitud;

import java.time.LocalDateTime;

public record SolicitudResponse(
        Long id,
        Long clienteId,
        String clienteNombre,
        Long categoriaId,
        String categoriaNombre,
        String descripcion,
        String direccion,
        EstadoSolicitud estado,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
