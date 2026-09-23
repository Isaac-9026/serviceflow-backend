package com.serviceflow.auditoria.entity;

import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registros_auditoria")
@Getter
@Setter
@NoArgsConstructor
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(name = "tipo_entidad", nullable = false, length = 50)
    private String tipoEntidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(columnDefinition = "TEXT")
    private String detalles;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}
