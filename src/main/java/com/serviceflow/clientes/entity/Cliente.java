package com.serviceflow.clientes.entity;

import com.serviceflow.comun.entity.EntidadBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends EntidadBase {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String telefono;

    @Column
    private String email;

    @Column
    private String direccion;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(nullable = false)
    private Boolean activo = true;
}
