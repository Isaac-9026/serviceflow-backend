package com.serviceflow.operacion.entity;

import com.serviceflow.catalogo.entity.CategoriaServicio;
import com.serviceflow.clientes.entity.Cliente;
import com.serviceflow.comun.entity.EntidadBase;
import com.serviceflow.identidad.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitudes_servicio")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudServicio extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaServicio categoria;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoSolicitud estado = EstadoSolicitud.RECIBIDA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
}
