package com.prueba.tecnica.Sof.service.impl;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.tecnica.Sof.dto.ActivoRequestDTO;
import com.prueba.tecnica.Sof.dto.ActivoResponseDTO;
import com.prueba.tecnica.Sof.dto.repository.ActivoRepository;
import com.prueba.tecnica.Sof.dto.repository.CategoriaRepository;
import com.prueba.tecnica.Sof.exception.BusinessException;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;
import com.prueba.tecnica.Sof.model.Activo;
import com.prueba.tecnica.Sof.model.Categoria;
import com.prueba.tecnica.Sof.service.ActivoService;

@Service
public class ActivoServiceImpl implements ActivoService {

    @Autowired
    private ActivoRepository activoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public ActivoResponseDTO crearActivo(ActivoRequestDTO dto) {
        if (activoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new BusinessException("El número de serie ya se encuentra registrado.");
        }

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new BusinessException("La categoría especificada no existe.")); 

        Activo activo = new Activo();
        activo.setNumeroSerie(dto.getNumeroSerie());
        activo.setMarcaModelo(dto.getMarcaModelo());
        activo.setEstado(dto.getEstado());
        activo.setCosto(dto.getCosto());
        activo.setCategoria(categoria);

        // Generación del Folio de Negocio Automático: [PREFIJO]-[AÑO]-[CONSECUTIVO]
        int anioActual = LocalDate.now().getYear();
        long consecutivo = activoRepository.countByCategoriaAndAnio(categoria.getId(), anioActual) + 1;
        String folio = String.format("%s-%d-%03d", categoria.getPrefijo(), anioActual, consecutivo); 
        activo.setFolio(folio); 

        return entityToResponseDTO(activoRepository.save(activo));
    }

    @Override
    @Transactional
    public ActivoResponseDTO actualizarActivo(UUID id, ActivoRequestDTO dto) {
        Activo activo = activoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("El activo no existe.")); 

        // Validación Obligatoria de Transición de Estados
        if ("Baja".equalsIgnoreCase(activo.getEstado()) && !dto.getEstado().equalsIgnoreCase("Baja")) {
            throw new BusinessException("Un activo con estado 'Baja' no podrá regresar a un estado operativo posterior.");
        }

        // Si cambia de categoría, las reglas comúnmente dicen mantener el folio original o reasignar.
        // Asumiendo que el folio permanece inmutable por auditoría, solo actualizamos los datos básicos.
        activo.setMarcaModelo(dto.getMarcaModelo());
        activo.setEstado(dto.getEstado());
        activo.setCosto(dto.getCosto());
        
        return entityToResponseDTO(activoRepository.save(activo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivoResponseDTO> consultarActivos(ActivoSearchCriteria criteria, Pageable pageable) {
        Specification<Activo> spec = (root, query, cb) -> {
            // Esta lista ahora acepta correctamente los predicados de Jakarta JPA
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getNumeroSerie() != null && !criteria.getNumeroSerie().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("numeroSerie")), "%" + criteria.getNumeroSerie().toLowerCase() + "%"));
            }
            if (criteria.getMarcaModelo() != null && !criteria.getMarcaModelo().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("marcaModelo")), "%" + criteria.getMarcaModelo().toLowerCase() + "%"));
            }
            if (criteria.getIdCategoria() != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), criteria.getIdCategoria()));
            }
            if (criteria.getEstado() != null && !criteria.getEstado().isBlank()) {
                predicates.add(cb.equal(root.get("estado"), criteria.getEstado()));
            }
            if (criteria.getCostoMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("costo"), criteria.getCostoMin()));
            }
            if (criteria.getCostoMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("costo"), criteria.getCostoMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return activoRepository.findAll(spec, pageable).map(this::entityToResponseDTO);
    }

    private ActivoResponseDTO entityToResponseDTO(Activo activo) {
        ActivoResponseDTO dto = new ActivoResponseDTO();
        dto.setId(activo.getId());
        dto.setFolio(activo.getFolio());
        dto.setNumeroSerie(activo.getNumeroSerie());
        dto.setMarcaModelo(activo.getMarcaModelo());
        dto.setEstado(activo.getEstado());
        dto.setCosto(activo.getCosto());
        dto.setFechaIngreso(activo.getFechaIngreso());
        dto.setIdCategoria(activo.getCategoria().getId()); 
        dto.setNombreCategoria(activo.getCategoria().getNombre()); 
        return dto;
    }
}