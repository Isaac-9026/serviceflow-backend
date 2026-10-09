package com.serviceflow.operacion.controller;

import com.serviceflow.operacion.dto.CotizacionResponse;
import com.serviceflow.operacion.entity.Cotizacion;
import com.serviceflow.operacion.service.CotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionConsultaController {

    private final CotizacionService cotizacionService;

    @GetMapping
    public ResponseEntity<List<CotizacionResponse>> listarCotizaciones() {
        List<Cotizacion> cotizaciones = cotizacionService.listarCotizaciones();
        List<CotizacionResponse> response = cotizaciones.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
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
