package com.serviceflow.auditoria.service;

import com.serviceflow.auditoria.entity.RegistroAuditoria;
import com.serviceflow.auditoria.repository.RegistroAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final RegistroAuditoriaRepository auditoriaRepository;

    @Transactional
    public RegistroAuditoria registrarAccion(RegistroAuditoria registro) {
        return auditoriaRepository.save(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAuditoria> obtenerHistorial(String tipoEntidad, Long entidadId) {
        return auditoriaRepository.findByTipoEntidadAndEntidadId(tipoEntidad, entidadId);
    }
}
