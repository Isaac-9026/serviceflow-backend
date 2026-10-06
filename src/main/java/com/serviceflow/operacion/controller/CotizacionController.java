package com.serviceflow.operacion.controller;

import com.serviceflow.operacion.dto.CotizacionResponse;
import com.serviceflow.operacion.dto.CrearCotizacionRequest;
import com.serviceflow.operacion.entity.Cotizacion;
import com.serviceflow.operacion.service.CotizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes/{solicitudId}/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<CotizacionResponse> crearCotizacion(
            @PathVariable Long solicitudId,
            @Valid @RequestBody CrearCotizacionRequest request) {
            
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setDescripcion(request.descripcion());
        cotizacion.setMonto(request.monto());
        cotizacion.setValidoHasta(request.validoHasta());

        Cotizacion creada = cotizacionService.crearCotizacion(solicitudId, cotizacion);
        return new ResponseEntity<>(mapToResponse(creada), HttpStatus.CREATED);
    }

    @PatchMapping("/{cotizacionId}/aprobar")
    public ResponseEntity<Void> aprobarCotizacion(
            @PathVariable Long solicitudId,
            @PathVariable Long cotizacionId) {
        
        cotizacionService.aprobarCotizacion(cotizacionId);
        return ResponseEntity.noContent().build();
    }

    private CotizacionResponse mapToResponse(Cotizacion cotizacion) {
        return new CotizacionResponse(
                cotizacion.getId(),
                cotizacion.getSolicitud().getId(),
                cotizacion.getDescripcion(),
                cotizacion.getMonto(),
                cotizacion.getValidoHasta(),
                cotizacion.getEstado(),
                cotizacion.getCreadoEn(),
                cotizacion.getActualizadoEn()
        );
    }
}
