package com.serviceflow.operacion.controller;

import com.serviceflow.identidad.dto.UsuarioResponse;
import com.serviceflow.operacion.dto.*;
import com.serviceflow.operacion.entity.Evidencia;
import com.serviceflow.operacion.entity.OrdenTrabajo;
import com.serviceflow.operacion.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> listarOrdenes() {
        List<OrdenTrabajo> ordenes = ordenTrabajoService.listarOrdenes();
        List<OrdenResponse> response = ordenes.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerPorId(@PathVariable Long id) {
        OrdenTrabajo orden = ordenTrabajoService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(orden));
    }

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

    @PostMapping(value = "/{id}/evidencias/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EvidenciaResponse> subirEvidencia(
            @PathVariable Long id,
            @RequestParam("tipo") com.serviceflow.operacion.entity.TipoEvidencia tipo,
            @RequestParam("nota") String nota,
            @RequestParam(value = "archivo", required = false) org.springframework.web.multipart.MultipartFile archivo) {
        
        Evidencia creada = ordenTrabajoService.registrarEvidencia(id, tipo, nota, archivo);
        return new ResponseEntity<>(mapEvidenciaToResponse(creada), HttpStatus.CREATED);
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

    @GetMapping("/{id}/evidencias")
    public ResponseEntity<List<EvidenciaResponse>> listarEvidencias(@PathVariable Long id) {
        List<Evidencia> evidencias = ordenTrabajoService.listarEvidencias(id);
        List<EvidenciaResponse> response = evidencias.stream()
                .map(this::mapEvidenciaToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarOrden(@PathVariable Long id) {
        ordenTrabajoService.completarOrden(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOrden(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo,
            @RequestBody(required = false) java.util.Map<String, String> body) {
        String motivoFinal = motivo;
        if ((motivoFinal == null || motivoFinal.isBlank()) && body != null && body.containsKey("motivo")) {
            motivoFinal = body.get("motivo");
        }
        ordenTrabajoService.cancelarOrden(id, motivoFinal);
        return ResponseEntity.noContent().build();
    }

    private OrdenResponse mapToResponse(OrdenTrabajo orden) {
        Long cotizacionId = orden.getCotizacion() != null ? orden.getCotizacion().getId() : null;
        UsuarioResponse tecnicoAsignado = ordenTrabajoService.obtenerTecnicoPrincipal(orden.getId())
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getEmail(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getRol(),
                        u.getActivo(),
                        u.getCreadoEn(),
                        u.getActualizadoEn()
                ))
                .orElse(null);
        
        return new OrdenResponse(
                orden.getId(),
                orden.getSolicitud().getId(),
                cotizacionId,
                orden.getDescripcion(),
                orden.getFechaProgramada(),
                orden.getEstado(),
                tecnicoAsignado,
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
