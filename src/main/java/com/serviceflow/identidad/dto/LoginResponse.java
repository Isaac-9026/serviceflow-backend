package com.serviceflow.identidad.dto;

public record LoginResponse(
        String token,
        UsuarioResponse usuario
) {}
