package com.serviceflow.operacion.service;

import com.serviceflow.auditoria.service.AuditoriaService;
import com.serviceflow.comun.exception.BusinessRuleException;
import com.serviceflow.comun.exception.ResourceNotFoundException;
import com.serviceflow.operacion.entity.Cotizacion;
import com.serviceflow.operacion.entity.EstadoCotizacion;
import com.serviceflow.operacion.entity.EstadoSolicitud;
import com.serviceflow.operacion.entity.SolicitudServicio;
import com.serviceflow.operacion.repository.CotizacionRepository;
import com.serviceflow.operacion.repository.SolicitudServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final SolicitudServicioRepository solicitudServicioRepository;
    private final AuditoriaService auditoriaService;

    @Transactional
    public Cotizacion crearCotizacion(Long solicitudId, Cotizacion cotizacion) {
        SolicitudServicio solicitud = solicitudServicioRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        if (solicitud.getEstado() == EstadoSolicitud.CANCELADA || solicitud.getEstado() == EstadoSolicitud.COMPLETADA) {
            throw new BusinessRuleException("No se puede cotizar una solicitud en estado: " + solicitud.getEstado());
        }

        cotizacion.setSolicitud(solicitud);
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);
        
        solicitud.setEstado(EstadoSolicitud.COTIZADA);
        solicitudServicioRepository.save(solicitud);

        Cotizacion guardada = cotizacionRepository.save(cotizacion);

        auditoriaService.registrar(
                "COTIZACION", 
                guardada.getId(), 
                "CREAR_COTIZACION", 
                "Cotización registrada con monto S/ " + guardada.getMonto() + " para solicitud #" + solicitudId
        );
        auditoriaService.registrar(
                "SOLICITUD", 
                solicitud.getId(), 
                "COTIZAR_SOLICITUD", 
                "Solicitud cotizada con cotización #" + guardada.getId()
        );

        return guardada;
    }

    @Transactional
    public void aprobarCotizacion(Long cotizacionId) {
        Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización no encontrada con id: " + cotizacionId));

        if (cotizacion.getEstado() != EstadoCotizacion.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden aprobar cotizaciones en estado PENDIENTE.");
        }

        cotizacion.setEstado(EstadoCotizacion.APROBADA);
        
        cotizacionRepository.save(cotizacion);

        auditoriaService.registrar(
                "COTIZACION", 
                cotizacion.getId(), 
                "APROBAR_COTIZACION", 
                "Cotización #" + cotizacion.getId() + " aprobada formalmente"
        );
    }

    @Transactional(readOnly = true)
    public java.util.List<Cotizacion> listarCotizaciones() {
        return cotizacionRepository.findAll();
    }
}
