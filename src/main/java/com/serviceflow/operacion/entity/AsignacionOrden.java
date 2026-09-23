package com.serviceflow.operacion.entity;

import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_orden")
@Getter
@Setter
@NoArgsConstructor
public class AsignacionOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenTrabajo orden;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Usuario tecnico;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @Column(name = "asignado_en", updatable = false)
    private LocalDateTime asignadoEn = LocalDateTime.now();
}
