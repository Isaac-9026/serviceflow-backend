package com.serviceflow.catalogo.dto;

public record CategoriaResponse(
        Long id,
        String nombre,
        boolean activo
) {}
