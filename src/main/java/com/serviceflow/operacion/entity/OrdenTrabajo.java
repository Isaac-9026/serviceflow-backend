package com.serviceflow.operacion.entity;

import com.serviceflow.comun.entity.EntidadBase;
import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_trabajo")
@Getter
@Setter
@NoArgsConstructor
public class OrdenTrabajo extends EntidadBase {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false, unique = true)
    private SolicitudServicio solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id")
    private Cotizacion cotizacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoOrden estado = EstadoOrden.CREADA;

    @Column(name = "completado_en")
    private LocalDateTime completadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
}
