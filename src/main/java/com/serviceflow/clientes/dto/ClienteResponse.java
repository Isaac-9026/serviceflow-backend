package com.serviceflow.clientes.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String telefono,
        String email,
        String direccion,
        String notas,
        boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
