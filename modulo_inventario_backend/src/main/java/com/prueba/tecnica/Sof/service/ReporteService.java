package com.prueba.tecnica.Sof.service;

import com.prueba.tecnica.Sof.dto.ReporteResponseDTO;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;

public interface ReporteService {
    ReporteResponseDTO generarReporteZip(ActivoSearchCriteria criteria, String usuarioSolicitante);
}
