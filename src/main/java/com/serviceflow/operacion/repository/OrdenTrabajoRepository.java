package com.serviceflow.operacion.repository;

import com.serviceflow.operacion.entity.OrdenTrabajo;
import com.serviceflow.operacion.entity.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    
    Optional<OrdenTrabajo> findBySolicitudId(Long solicitudId);
    
    List<OrdenTrabajo> findByEstado(EstadoOrden estado);
}
