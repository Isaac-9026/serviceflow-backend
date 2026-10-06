package com.serviceflow.operacion.dto;

import com.serviceflow.operacion.entity.EstadoCotizacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CotizacionResponse(
        Long id,
        Long solicitudId,
        String descripcion,
        BigDecimal monto,
        LocalDate validoHasta,
        EstadoCotizacion estado,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
