package com.serviceflow.operacion.controller;

import com.serviceflow.operacion.dto.*;
import com.serviceflow.operacion.entity.Evidencia;
import com.serviceflow.operacion.entity.OrdenTrabajo;
import com.serviceflow.operacion.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenTrabajoService ordenTrabajoService;

    @PostMapping
    public ResponseEntity<OrdenResponse> crearOrden(@Valid @RequestBody CrearOrdenRequest request) {
        OrdenTrabajo orden = ordenTrabajoService.crearOrden(
                request.solicitudId(),
                request.cotizacionId(),
                request.descripcion()
        );
        return new ResponseEntity<>(mapToResponse(orden), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/asignar")
    public ResponseEntity<Void> asignarTecnico(
            @PathVariable Long id,
            @Valid @RequestBody AsignarTecnicoRequest request) {
        
        ordenTrabajoService.asignarTecnico(id, request.tecnicoId(), request.esPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/evidencias")
    public ResponseEntity<EvidenciaResponse> agregarEvidencia(
            @PathVariable Long id,
            @Valid @RequestBody AgregarEvidenciaRequest request) {
        
        Evidencia evidencia = new Evidencia();
        evidencia.setTipo(request.tipo());
        evidencia.setRutaArchivo(request.rutaArchivo());
        evidencia.setNota(request.nota());

        Evidencia creada = ordenTrabajoService.agregarEvidencia(id, evidencia);
        return new ResponseEntity<>(mapEvidenciaToResponse(creada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarOrden(@PathVariable Long id) {
        ordenTrabajoService.completarOrden(id);
        return ResponseEntity.noContent().build();
    }

    private OrdenResponse mapToResponse(OrdenTrabajo orden) {
        Long cotizacionId = orden.getCotizacion() != null ? orden.getCotizacion().getId() : null;
        
        return new OrdenResponse(
                orden.getId(),
                orden.getSolicitud().getId(),
                cotizacionId,
                orden.getDescripcion(),
                orden.getFechaProgramada(),
                orden.getEstado(),
                orden.getCompletadoEn(),
                orden.getCreadoEn(),
                orden.getActualizadoEn()
        );
    }

    private EvidenciaResponse mapEvidenciaToResponse(Evidencia evidencia) {
        return new EvidenciaResponse(
                evidencia.getId(),
                evidencia.getOrden().getId(),
                evidencia.getTipo(),
                evidencia.getRutaArchivo(),
                evidencia.getNota(),
                evidencia.getCreadoEn()
        );
    }
}
