package com.serviceflow.identidad.dto;

import com.serviceflow.identidad.entity.RolUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String email,
        String nombre,
        String apellido,
        RolUsuario rol,
        boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
