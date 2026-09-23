package com.serviceflow.catalogo.repository;

import com.serviceflow.catalogo.entity.CategoriaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaServicioRepository extends JpaRepository<CategoriaServicio, Long> {
    
    Optional<CategoriaServicio> findByNombre(String nombre);
    
    List<CategoriaServicio> findByActivoTrue();
}
