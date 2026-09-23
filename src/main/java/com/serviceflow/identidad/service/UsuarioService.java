package com.serviceflow.identidad.service;

import com.serviceflow.comun.exception.BusinessRuleException;
import com.serviceflow.comun.exception.ResourceNotFoundException;
import com.serviceflow.identidad.entity.PerfilTecnico;
import com.serviceflow.identidad.entity.RolUsuario;
import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.repository.PerfilTecnicoRepository;
import com.serviceflow.identidad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilTecnicoRepository perfilTecnicoRepository;

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new BusinessRuleException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        
        usuario.setActivo(true);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Si es tecnico... debe tener un perfil tecnico asociado.
        //Lo creamos vacio por defecto para que luego se pueda actualizar
        if (usuarioGuardado.getRol() == RolUsuario.TECNICO) {
            PerfilTecnico perfil = new PerfilTecnico();
            perfil.setUsuario(usuarioGuardado);
            perfil.setEspecialidades("General"); //defecto
            perfilTecnicoRepository.save(perfil);
        }
        
        return usuarioGuardado;
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);
        
        //Verificar si el email cambio y si ya esta en uso
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) && 
            usuarioRepository.findByEmail(usuarioActualizado.getEmail()).isPresent()) {
            throw new BusinessRuleException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
        }
        
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellido(usuarioActualizado.getApellido());
        usuario.setEmail(usuarioActualizado.getEmail());
        
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }
}
