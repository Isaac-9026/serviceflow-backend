package com.serviceflow.operacion.repository;

import com.serviceflow.operacion.entity.AsignacionOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionOrdenRepository extends JpaRepository<AsignacionOrden, Long> {
    
    List<AsignacionOrden> findByOrdenId(Long ordenId);
    
    List<AsignacionOrden> findByTecnicoId(Long tecnicoId);
}
