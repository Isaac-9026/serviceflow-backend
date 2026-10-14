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
    public ResponseEntity<List<CategoriaResponse>> obtenerCategorias(@RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        List<CategoriaServicio> categorias = soloActivas 
                ? categoriaServicioService.obtenerTodasActivas() 
                : categoriaServicioService.obtenerTodas();
        List<CategoriaResponse> response = categorias.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
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

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CrearCategoriaRequest request) {
        CategoriaServicio actualizada = categoriaServicioService.actualizarCategoria(id, request.nombre());
        return ResponseEntity.ok(mapToResponse(actualizada));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean activo,
            @RequestBody(required = false) java.util.Map<String, Boolean> body) {
        boolean nuevoEstado = activo != null ? activo : (body != null && body.containsKey("activo") ? body.get("activo") : true);
        categoriaServicioService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.noContent().build();
    }

    private CategoriaResponse mapToResponse(CategoriaServicio categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getActivo()
        );
    }
}
