package com.prueba.tecnica.Sof.service;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.prueba.tecnica.Sof.dto.ActivoRequestDTO;
import com.prueba.tecnica.Sof.dto.ActivoResponseDTO;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;

public interface ActivoService {
    
    
    ActivoResponseDTO crearActivo(ActivoRequestDTO dto);
    
    
    ActivoResponseDTO actualizarActivo(UUID id, ActivoRequestDTO dto);
    
    
    Page<ActivoResponseDTO> consultarActivos(ActivoSearchCriteria criteria, Pageable pageable);
}
