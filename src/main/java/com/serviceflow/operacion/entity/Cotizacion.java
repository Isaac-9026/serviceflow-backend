package com.serviceflow.operacion.entity;

import com.serviceflow.comun.entity.EntidadBase;
import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
@Getter
@Setter
@NoArgsConstructor
public class Cotizacion extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudServicio solicitud;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "valido_hasta")
    private LocalDate validoHasta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoCotizacion estado = EstadoCotizacion.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobado_por")
    private Usuario aprobadoPor;

    @Column(name = "aprobado_en")
    private LocalDateTime aprobadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
}
