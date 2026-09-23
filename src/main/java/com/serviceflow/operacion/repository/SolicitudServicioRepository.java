package com.serviceflow.operacion.repository;

import com.serviceflow.operacion.entity.SolicitudServicio;
import com.serviceflow.operacion.entity.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepository extends JpaRepository<SolicitudServicio, Long> {
    
    List<SolicitudServicio> findByClienteId(Long clienteId);
    
    List<SolicitudServicio> findByEstado(EstadoSolicitud estado);
}
