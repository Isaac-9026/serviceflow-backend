package com.serviceflow.operacion.repository;

import com.serviceflow.operacion.entity.Cotizacion;
import com.serviceflow.operacion.entity.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    
    List<Cotizacion> findBySolicitudId(Long solicitudId);
    
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}
