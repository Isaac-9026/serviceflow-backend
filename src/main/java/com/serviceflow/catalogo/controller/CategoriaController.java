package com.serviceflow.catalogo.controller;

import com.serviceflow.catalogo.dto.CategoriaResponse;
import com.serviceflow.catalogo.dto.CrearCategoriaRequest;
import com.serviceflow.catalogo.entity.CategoriaServicio;
import com.serviceflow.catalogo.service.CategoriaServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaServicioService categoriaServicioService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodasActivas() {
        List<CategoriaResponse> categorias = categoriaServicioService.obtenerTodasActivas().stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        CategoriaServicio categoria = categoriaServicioService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CrearCategoriaRequest request) {
        CategoriaServicio categoria = new CategoriaServicio();
        categoria.setNombre(request.nombre());
        
        CategoriaServicio creada = categoriaServicioService.crearCategoria(categoria);
        return new ResponseEntity<>(mapToResponse(creada), HttpStatus.CREATED);
    }

    private CategoriaResponse mapToResponse(CategoriaServicio categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getActivo()
        );
    }
}
