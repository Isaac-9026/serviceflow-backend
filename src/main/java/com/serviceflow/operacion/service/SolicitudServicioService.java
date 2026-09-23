package com.serviceflow.operacion.service;

import com.serviceflow.catalogo.entity.CategoriaServicio;
import com.serviceflow.catalogo.repository.CategoriaServicioRepository;
import com.serviceflow.clientes.entity.Cliente;
import com.serviceflow.clientes.repository.ClienteRepository;
import com.serviceflow.comun.exception.BusinessRuleException;
import com.serviceflow.comun.exception.ResourceNotFoundException;
import com.serviceflow.operacion.entity.EstadoSolicitud;
import com.serviceflow.operacion.entity.SolicitudServicio;
import com.serviceflow.operacion.repository.SolicitudServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServicioService {

    private final SolicitudServicioRepository solicitudServicioRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaServicioRepository categoriaServicioRepository;

    @Transactional(readOnly = true)
    public List<SolicitudServicio> obtenerTodas() {
        return solicitudServicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SolicitudServicio obtenerPorId(Long id) {
        return solicitudServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));
    }

    @Transactional
    public SolicitudServicio crearSolicitud(Long clienteId, Long categoriaId, SolicitudServicio solicitud) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));
                
        CategoriaServicio categoria = categoriaServicioRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + categoriaId));

        solicitud.setCliente(cliente);
        solicitud.setCategoria(categoria);
        solicitud.setEstado(EstadoSolicitud.RECIBIDA);
        
        return solicitudServicioRepository.save(solicitud);
    }
    
    @Transactional
    public void cancelarSolicitud(Long id, String motivo) {
        SolicitudServicio solicitud = obtenerPorId(id);
        
        if (solicitud.getEstado() == EstadoSolicitud.COMPLETADA || solicitud.getEstado() == EstadoSolicitud.CANCELADA) {
            throw new BusinessRuleException("No se puede cancelar una solicitud en estado: " + solicitud.getEstado());
        }
        
        solicitud.setEstado(EstadoSolicitud.CANCELADA);
        //Si tuvieramos campo para motivo de cancelación se agregaría aquí.
        solicitudServicioRepository.save(solicitud);
    }
}
