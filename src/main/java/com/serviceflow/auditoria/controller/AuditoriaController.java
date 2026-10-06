package com.serviceflow.auditoria.controller;

import com.serviceflow.auditoria.dto.RegistroAuditoriaResponse;
import com.serviceflow.auditoria.entity.RegistroAuditoria;
import com.serviceflow.auditoria.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

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
        
        return new RegistroAuditoriaResponse(
                registro.getId(),
                usuarioId,
                registro.getAccion(),
                registro.getTipoEntidad(),
                registro.getEntidadId(),
                registro.getDetalles(),
                registro.getCreadoEn()
        );
    }
}
