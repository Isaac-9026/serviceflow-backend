package com.serviceflow.identidad.controller;

import com.serviceflow.comun.security.JwtService;
import com.serviceflow.identidad.dto.LoginRequest;
import com.serviceflow.identidad.dto.LoginResponse;
import com.serviceflow.identidad.dto.UsuarioResponse;
import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Autenticar (lanza excepción si las credenciales son inválidas)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // Si llega aquí, es válido. Generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String jwtToken = jwtService.generateToken(userDetails);

        // Obtener la entidad Usuario para devolver los datos en el response
        // Como pasó el login, estamos seguros de que existe
        Usuario usuario = usuarioRepository.findByEmail(request.email()).orElseThrow();
        
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getCreadoEn(),
                usuario.getActualizadoEn()
        );

        return ResponseEntity.ok(new LoginResponse(jwtToken, usuarioResponse));
    }
}
