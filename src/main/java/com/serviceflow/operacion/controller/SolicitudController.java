package com.serviceflow.operacion.controller;

import com.serviceflow.operacion.dto.CrearSolicitudRequest;
import com.serviceflow.operacion.dto.SolicitudResponse;
import com.serviceflow.operacion.entity.SolicitudServicio;
import com.serviceflow.operacion.service.SolicitudServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudServicioService solicitudServicioService;

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> obtenerTodas() {
        List<SolicitudResponse> solicitudes = solicitudServicioService.obtenerTodas().stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerPorId(@PathVariable Long id) {
        SolicitudServicio solicitud = solicitudServicioService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(solicitud));
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(@Valid @RequestBody CrearSolicitudRequest request) {
        SolicitudServicio solicitud = new SolicitudServicio();
        solicitud.setDescripcion(request.descripcion());
        solicitud.setDireccion(request.direccion());

        SolicitudServicio creada = solicitudServicioService.crearSolicitud(
                request.clienteId(), 
                request.categoriaId(), 
                solicitud
        );
        
        return new ResponseEntity<>(mapToResponse(creada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSolicitud(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        solicitudServicioService.cancelarSolicitud(id, motivo);
        return ResponseEntity.noContent().build();
    }

    private SolicitudResponse mapToResponse(SolicitudServicio solicitud) {
        Long categoriaId = solicitud.getCategoria() != null ? solicitud.getCategoria().getId() : null;
        String categoriaNombre = solicitud.getCategoria() != null ? solicitud.getCategoria().getNombre() : null;
        
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getCliente().getId(),
                solicitud.getCliente().getNombre(),
                categoriaId,
                categoriaNombre,
                solicitud.getDescripcion(),
                solicitud.getDireccion(),
                solicitud.getEstado(),
                solicitud.getCreadoEn(),
                solicitud.getActualizadoEn()
        );
    }
}
