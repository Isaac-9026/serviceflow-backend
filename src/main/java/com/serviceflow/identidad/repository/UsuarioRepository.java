package com.serviceflow.identidad.repository;

import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.entity.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByRol(RolUsuario rol);
    
    List<Usuario> findByActivoTrue();
}
