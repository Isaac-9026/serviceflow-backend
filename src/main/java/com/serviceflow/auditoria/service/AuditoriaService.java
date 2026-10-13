package com.serviceflow.auditoria.service;

import com.serviceflow.auditoria.entity.RegistroAuditoria;
import com.serviceflow.auditoria.repository.RegistroAuditoriaRepository;
import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final RegistroAuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public RegistroAuditoria registrarAccion(RegistroAuditoria registro) {
        return auditoriaRepository.save(registro);
    }

    @Transactional
    public void registrar(String tipoEntidad, Long entidadId, String accion, String detalles) {
        Usuario usuario = null;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                usuario = usuarioRepository.findByEmail(auth.getName()).orElse(null);
            }
        } catch (Exception ignored) {
        }

        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setTipoEntidad(tipoEntidad);
        registro.setEntidadId(entidadId);
        registro.setAccion(accion);
        registro.setDetalles(detalles);
        registro.setUsuario(usuario);
        registro.setCreadoEn(LocalDateTime.now());

        auditoriaRepository.save(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAuditoria> obtenerHistorial(String tipoEntidad, Long entidadId) {
        return auditoriaRepository.findByTipoEntidadAndEntidadIdOrderByCreadoEnDesc(tipoEntidad, entidadId);
    }

    @Transactional(readOnly = true)
    public List<RegistroAuditoria> listarTodos() {
        return auditoriaRepository.findAllByOrderByCreadoEnDesc();
    }
}
