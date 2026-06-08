package com.prueba.tecnica.Sof.controllers;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.tecnica.Sof.dto.ActivoRequestDTO;
import com.prueba.tecnica.Sof.dto.ActivoResponseDTO;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;
import com.prueba.tecnica.Sof.service.ActivoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activos")
@CrossOrigin(origins = "*") // Permite la comunicación con Angular durante el desarrollo local
public class ActivoController {

    @Autowired
    private ActivoService activoService;

    // 3.1 Consulta y Búsqueda con Filtros, Paginación y Ordenamiento Configurable
    @GetMapping
    public ResponseEntity<Page<ActivoResponseDTO>> listarActivos(
            @RequestParam(required = false) String numeroSerie,
            @RequestParam(required = false) String marcaModelo,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) BigDecimal costoMin,
            @RequestParam(required = false) BigDecimal costoMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaIngreso") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Construir los criterios de búsqueda
        ActivoSearchCriteria criteria = new ActivoSearchCriteria();
        criteria.setNumeroSerie(numeroSerie);
        criteria.setMarcaModelo(marcaModelo);
        criteria.setIdCategoria(idCategoria);
        criteria.setEstado(estado);
        criteria.setCostoMin(costoMin);
        criteria.setCostoMax(costoMax);

        // Configurar ordenamiento dinámico
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        Pageable pageable = (Pageable) PageRequest.of(page, size, sort);

        return ResponseEntity.ok(activoService.consultarActivos(criteria, pageable));
    }

    // 3.2 Operación de Escritura: Registro de nuevos activos
    @PostMapping
    public ResponseEntity<ActivoResponseDTO> guardarActivo(@Valid @RequestBody ActivoRequestDTO dto) {
        ActivoResponseDTO nuevoActivo = activoService.crearActivo(dto);
        return new ResponseEntity(nuevoActivo, HttpStatus.CREATED);
    }

    // 3.2 Operación de Escritura: Actualización de activos existentes
    @PutMapping("/{id}")
    public ResponseEntity<ActivoResponseDTO> actualizarActivo(
            @PathVariable UUID id,
            @Valid @RequestBody ActivoRequestDTO dto) {
        return ResponseEntity.ok(activoService.actualizarActivo(id, dto));
    }
}
