package com.serviceflow.auditoria.repository;

import com.serviceflow.auditoria.entity.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    
    List<RegistroAuditoria> findByTipoEntidadAndEntidadId(String tipoEntidad, Long entidadId);
}
