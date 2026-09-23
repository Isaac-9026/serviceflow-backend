package com.serviceflow.identidad.repository;

import com.serviceflow.identidad.entity.PerfilTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilTecnicoRepository extends JpaRepository<PerfilTecnico, Long> {
    
    Optional<PerfilTecnico> findByUsuarioId(Long usuarioId);
}
