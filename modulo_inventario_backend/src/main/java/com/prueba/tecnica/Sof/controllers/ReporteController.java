package com.prueba.tecnica.Sof.controllers;



import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.tecnica.Sof.dto.ReporteResponseDTO;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;
import com.prueba.tecnica.Sof.service.ReporteService;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/exportar")
    public ResponseEntity<ReporteResponseDTO> exportarReporteZip(
            @RequestParam(required = false) String numeroSerie,
            @RequestParam(required = false) String marcaModelo,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) BigDecimal costoMin,
            @RequestParam(required = false) BigDecimal costoMax) {

        ActivoSearchCriteria criteria = new ActivoSearchCriteria();
        criteria.setNumeroSerie(numeroSerie);
        criteria.setMarcaModelo(marcaModelo);
        criteria.setIdCategoria(idCategoria);
        criteria.setEstado(estado);
        criteria.setCostoMin(costoMin);
        criteria.setCostoMax(costoMax);

        // TODO: En la fase de seguridad, se obtendrá del SecurityContextHolder.getContext().getAuthentication().getName()
        String usuarioActual = "Usuario_Auditor"; 

        ReporteResponseDTO reporte = reporteService.generarReporteZip(criteria, usuarioActual);
        return ResponseEntity.ok(reporte);
    }
}