package com.serviceflow.operacion.entity;

import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias")
@Getter
@Setter
@NoArgsConstructor
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenTrabajo orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoEvidencia tipo;

    @Column(name = "ruta_archivo", length = 500)
    private String rutaArchivo;

    @Column(columnDefinition = "TEXT")
    private String nota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}
