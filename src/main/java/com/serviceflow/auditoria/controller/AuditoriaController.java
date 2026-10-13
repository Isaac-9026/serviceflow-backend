package com.serviceflow.auditoria.controller;

import com.serviceflow.auditoria.dto.RegistroAuditoriaResponse;
import com.serviceflow.auditoria.entity.RegistroAuditoria;
import com.serviceflow.auditoria.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<RegistroAuditoriaResponse>> listarTodos(
            @RequestParam(required = false) String tipoEntidad,
            @RequestParam(required = false) Long entidadId) {
        
        List<RegistroAuditoria> logs;
        if (tipoEntidad != null && !tipoEntidad.isBlank() && entidadId != null) {
            logs = auditoriaService.obtenerHistorial(tipoEntidad, entidadId);
        } else {
            logs = auditoriaService.listarTodos();
        }

        List<RegistroAuditoriaResponse> response = logs.stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tipoEntidad}/{entidadId}")
    public ResponseEntity<List<RegistroAuditoriaResponse>> obtenerHistorial(
            @PathVariable String tipoEntidad,
            @PathVariable Long entidadId) {
        
        List<RegistroAuditoriaResponse> historial = auditoriaService.obtenerHistorial(tipoEntidad, entidadId).stream()
                .map(this::mapToResponse)
                .toList();
                
        return ResponseEntity.ok(historial);
    }

    private RegistroAuditoriaResponse mapToResponse(RegistroAuditoria registro) {
        Long usuarioId = registro.getUsuario() != null ? registro.getUsuario().getId() : null;
        String usuarioNombre = null;
        String usuarioEmail = null;

        if (registro.getUsuario() != null) {
            usuarioNombre = (registro.getUsuario().getNombre() + " " + 
                    (registro.getUsuario().getApellido() != null ? registro.getUsuario().getApellido() : "")).trim();
            usuarioEmail = registro.getUsuario().getEmail();
        }
        
        return new RegistroAuditoriaResponse(
                registro.getId(),
                usuarioId,
                usuarioNombre,
                usuarioEmail,
                registro.getAccion(),
                registro.getTipoEntidad(),
                registro.getEntidadId(),
                registro.getDetalles(),
                registro.getCreadoEn()
        );
    }
}
