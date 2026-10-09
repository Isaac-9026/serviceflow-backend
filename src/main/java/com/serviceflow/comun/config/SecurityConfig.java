package com.serviceflow.comun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.serviceflow.comun.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF (en este caso no esnecesario en Apis Rest con JWT)
            .csrf(AbstractHttpConfigurer::disable)
            // 2. Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 3. Configurar politica de sesion (sin estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 4. Configurar autorizacion de rutas base según matriz de permisos
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Módulo Usuarios y Auditoría (Solo ADMIN)
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/auditoria/**").hasRole("ADMIN")
                
                // Módulo Categorías (GET para todos, mutación solo ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAnyRole("ADMIN", "COORDINADOR", "TECNICO")
                .requestMatchers("/api/categorias/**").hasRole("ADMIN")
                
                // Módulo Clientes, Solicitudes, Cotizaciones (ADMIN y COORDINADOR)
                .requestMatchers("/api/clientes/**").hasAnyRole("ADMIN", "COORDINADOR")
                .requestMatchers("/api/solicitudes/**").hasAnyRole("ADMIN", "COORDINADOR")
                .requestMatchers("/api/cotizaciones", "/api/cotizaciones/**").hasAnyRole("ADMIN", "COORDINADOR")
                
                // Módulo Órdenes
                .requestMatchers(HttpMethod.POST, "/api/ordenes").hasAnyRole("ADMIN", "COORDINADOR")
                .requestMatchers("/api/ordenes", "/api/ordenes/**").hasAnyRole("ADMIN", "COORDINADOR", "TECNICO")
                
                // Cualquier otra petición requerirá autenticación por defecto
                .anyRequest().authenticated()
            )
            // 5. Añadir nuestro filtro antes del UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
