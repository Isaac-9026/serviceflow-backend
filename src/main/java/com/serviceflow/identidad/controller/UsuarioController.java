package com.serviceflow.identidad.controller;

import com.serviceflow.identidad.dto.ActualizarUsuarioRequest;
import com.serviceflow.identidad.dto.CrearUsuarioRequest;
import com.serviceflow.identidad.dto.UsuarioResponse;
import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<UsuarioResponse> usuarios = usuarioService.obtenerTodos().stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setRol(request.rol());

        Usuario creado = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(mapToResponse(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
            
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setEmail(request.email());
        usuarioActualizado.setNombre(request.nombre());
        usuarioActualizado.setApellido(request.apellido());

        Usuario guardado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(mapToResponse(guardado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        usuarioService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getCreadoEn(),
                usuario.getActualizadoEn()
        );
    }
}
