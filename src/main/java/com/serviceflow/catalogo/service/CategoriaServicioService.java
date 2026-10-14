package com.serviceflow.catalogo.service;

import com.serviceflow.catalogo.entity.CategoriaServicio;
import com.serviceflow.catalogo.repository.CategoriaServicioRepository;
import com.serviceflow.comun.exception.BusinessRuleException;
import com.serviceflow.comun.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServicioService {

    private final CategoriaServicioRepository categoriaServicioRepository;

    @Transactional(readOnly = true)
    public List<CategoriaServicio> obtenerTodas() {
        return categoriaServicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CategoriaServicio> obtenerTodasActivas() {
        return categoriaServicioRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public CategoriaServicio obtenerPorId(Long id) {
        return categoriaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    @Transactional
    public CategoriaServicio crearCategoria(CategoriaServicio categoria) {
        if (categoriaServicioRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new BusinessRuleException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        categoria.setActivo(true);
        return categoriaServicioRepository.save(categoria);
    }

    @Transactional
    public CategoriaServicio actualizarCategoria(Long id, String nuevoNombre) {
        CategoriaServicio categoria = obtenerPorId(id);
        if (!categoria.getNombre().equalsIgnoreCase(nuevoNombre) && 
            categoriaServicioRepository.findByNombre(nuevoNombre).isPresent()) {
            throw new BusinessRuleException("Ya existe una categoría con el nombre: " + nuevoNombre);
        }
        categoria.setNombre(nuevoNombre);
        return categoriaServicioRepository.save(categoria);
    }

    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        CategoriaServicio categoria = obtenerPorId(id);
        categoria.setActivo(activo);
        categoriaServicioRepository.save(categoria);
    }
}
