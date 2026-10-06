package com.serviceflow.identidad.service;

import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("El usuario está inactivo: " + email);
        }

        // Mapeamos nuestro RolUsuario a un GrantedAuthority de Spring Security
        // Spring Security espera que los roles tengan el prefijo "ROLE_"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(!usuario.getActivo()) // bloqueado si está inactivo
                .credentialsExpired(false)
                .disabled(!usuario.getActivo()) // deshabilitado si está inactivo
                .build();
    }
}
