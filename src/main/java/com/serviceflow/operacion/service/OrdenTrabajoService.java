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
    private final com.serviceflow.comun.storage.AlmacenamientoService almacenamientoService;
    private final com.serviceflow.auditoria.service.AuditoriaService auditoriaService;

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

        OrdenTrabajo guardada = ordenTrabajoRepository.save(orden);

        auditoriaService.registrar(
                "ORDEN",
                guardada.getId(),
                "CREAR_ORDEN",
                "Orden de trabajo creada a partir de solicitud #" + solicitudId + (cotizacionId != null ? " con cotización #" + cotizacionId : "")
        );
        auditoriaService.registrar(
                "SOLICITUD",
                solicitud.getId(),
                "GENERAR_ORDEN",
                "Orden de trabajo #" + guardada.getId() + " generada para atención de la solicitud"
        );

        return guardada;
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

        AsignacionOrden guardada = asignacionOrdenRepository.save(asignacion);

        auditoriaService.registrar(
                "ORDEN",
                orden.getId(),
                "ASIGNAR_TECNICO",
                "Técnico " + tecnico.getNombre() + " " + (tecnico.getApellido() != null ? tecnico.getApellido() : "") + " asignado como " + (esPrincipal ? "Principal" : "Secundario")
        );

        return guardada;
    }

    @Transactional
    public Evidencia registrarEvidencia(Long ordenId, TipoEvidencia tipo, String nota, org.springframework.web.multipart.MultipartFile archivo) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        String ruta = null;
        if (archivo != null && !archivo.isEmpty()) {
            ruta = almacenamientoService.guardar(archivo, "evidencias");
        }

        Evidencia evidencia = new Evidencia();
        evidencia.setOrden(orden);
        evidencia.setTipo(tipo);
        evidencia.setNota(nota);
        evidencia.setRutaArchivo(ruta);
        evidencia.setCreadoEn(LocalDateTime.now());

        orden.setEstado(EstadoOrden.PENDIENTE_VERIFICACION);
        ordenTrabajoRepository.save(orden);

        Evidencia guardada = evidenciaRepository.save(evidencia);

        auditoriaService.registrar(
                "ORDEN",
                orden.getId(),
                "REGISTRAR_EVIDENCIA",
                "Evidencia de campo registrada (" + tipo + "): " + (nota != null ? nota : "") + (ruta != null ? " [Archivo: " + ruta + "]" : "")
        );

        return guardada;
    }

    @Transactional
    public Evidencia agregarEvidencia(Long ordenId, Evidencia evidencia) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        evidencia.setOrden(orden);
        evidencia.setCreadoEn(LocalDateTime.now());

        orden.setEstado(EstadoOrden.PENDIENTE_VERIFICACION);
        ordenTrabajoRepository.save(orden);

        Evidencia guardada = evidenciaRepository.save(evidencia);

        auditoriaService.registrar(
                "ORDEN",
                orden.getId(),
                "REGISTRAR_EVIDENCIA",
                "Evidencia registrada (" + evidencia.getTipo() + "): " + (evidencia.getNota() != null ? evidencia.getNota() : "")
        );

        return guardada;
    }

    @Transactional(readOnly = true)
    public List<Evidencia> listarEvidencias(Long ordenId) {
        return evidenciaRepository.findByOrdenId(ordenId);
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

        auditoriaService.registrar(
                "ORDEN",
                orden.getId(),
                "COMPLETAR_ORDEN",
                "Orden de trabajo completada y verificada administrativamente"
        );
        auditoriaService.registrar(
                "SOLICITUD",
                solicitud.getId(),
                "COMPLETAR_SOLICITUD",
                "Solicitud completada satisfactoriamente con orden #" + orden.getId()
        );
    }

    @Transactional
    public void cancelarOrden(Long ordenId, String motivo) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id: " + ordenId));

        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            throw new BusinessRuleException("No se puede cancelar una orden que ya fue completada.");
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        ordenTrabajoRepository.save(orden);

        auditoriaService.registrar(
                "ORDEN",
                orden.getId(),
                "CANCELAR_ORDEN",
                "Orden de trabajo cancelada. Motivo: " + (motivo != null && !motivo.isBlank() ? motivo : "Cancelada por coordinación")
        );
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
