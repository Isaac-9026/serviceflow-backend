package com.serviceflow.operacion.service;

import com.serviceflow.comun.exception.BusinessRuleException;
import com.serviceflow.comun.exception.ResourceNotFoundException;
import com.serviceflow.identidad.entity.Usuario;
import com.serviceflow.identidad.repository.UsuarioRepository;
import com.serviceflow.operacion.entity.*;
import com.serviceflow.operacion.repository.AsignacionOrdenRepository;
import com.serviceflow.operacion.repository.CotizacionRepository;
import com.serviceflow.operacion.repository.EvidenciaRepository;
import com.serviceflow.operacion.repository.OrdenTrabajoRepository;
import com.serviceflow.operacion.repository.SolicitudServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final SolicitudServicioRepository solicitudServicioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final AsignacionOrdenRepository asignacionOrdenRepository;
    private final EvidenciaRepository evidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public OrdenTrabajo crearOrden(Long solicitudId, Long cotizacionId, String descripcion) {
        SolicitudServicio solicitud = solicitudServicioRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        if (solicitud.getEstado() == EstadoSolicitud.CANCELADA || solicitud.getEstado() == EstadoSolicitud.COMPLETADA) {
            throw new BusinessRuleException("No se puede crear una orden para una solicitud en estado: " + solicitud.getEstado());
        }

        if (ordenTrabajoRepository.findBySolicitudId(solicitudId).isPresent()) {
            throw new BusinessRuleException("Ya existe una orden de trabajo para esta solicitud.");
        }

        OrdenTrabajo orden = new OrdenTrabajo();
        orden.setSolicitud(solicitud);
        orden.setDescripcion(descripcion);
        orden.setEstado(EstadoOrden.CREADA);

        if (cotizacionId != null) {
            Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cotización no encontrada con id: " + cotizacionId));
            if (cotizacion.getEstado() != EstadoCotizacion.APROBADA) {
                throw new BusinessRuleException("La cotización debe estar aprobada para generar la orden.");
            }
            orden.setCotizacion(cotizacion);
        }

        solicitud.setEstado(EstadoSolicitud.EN_PROGRESO);
        solicitudServicioRepository.save(solicitud);

        return ordenTrabajoRepository.save(orden);
    }

    @Transactional
    public AsignacionOrden asignarTecnico(Long ordenId, Long tecnicoId, boolean esPrincipal) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico no encontrado con id: " + tecnicoId));

        AsignacionOrden asignacion = new AsignacionOrden();
        asignacion.setOrden(orden);
        asignacion.setTecnico(tecnico);
        asignacion.setEsPrincipal(esPrincipal);
        asignacion.setAsignadoEn(LocalDateTime.now());

        if (orden.getEstado() == EstadoOrden.CREADA) {
            orden.setEstado(EstadoOrden.EN_PROGRESO);
            ordenTrabajoRepository.save(orden);
        }

        return asignacionOrdenRepository.save(asignacion);
    }

    @Transactional
    public Evidencia agregarEvidencia(Long ordenId, Evidencia evidencia) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        evidencia.setOrden(orden);
        evidencia.setCreadoEn(LocalDateTime.now());

        orden.setEstado(EstadoOrden.PENDIENTE_VERIFICACION);
        ordenTrabajoRepository.save(orden);

        return evidenciaRepository.save(evidencia);
    }

    @Transactional
    public void completarOrden(Long ordenId) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        orden.setEstado(EstadoOrden.COMPLETADA);
        orden.setCompletadoEn(LocalDateTime.now());
        
        SolicitudServicio solicitud = orden.getSolicitud();
        solicitud.setEstado(EstadoSolicitud.COMPLETADA);
        solicitudServicioRepository.save(solicitud);

        ordenTrabajoRepository.save(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenes() {
        return ordenTrabajoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrdenTrabajo obtenerPorId(Long id) {
        return ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerTecnicoPrincipal(Long ordenId) {
        return asignacionOrdenRepository.findByOrdenId(ordenId).stream()
                .filter(AsignacionOrden::getEsPrincipal)
                .map(AsignacionOrden::getTecnico)
                .findFirst()
                .or(() -> asignacionOrdenRepository.findByOrdenId(ordenId).stream()
                        .map(AsignacionOrden::getTecnico)
                        .findFirst());
    }
}
