package com.serviceflow.operacion.repository;

import com.serviceflow.operacion.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
    
    List<Evidencia> findByOrdenId(Long ordenId);
}
