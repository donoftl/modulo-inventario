package com.prueba.tecnica.Sof.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActivoResponseDTO {
    private UUID id; 
    private String folio; 
    private String numeroSerie;
    private String marcaModelo;
    private String estado;
    private BigDecimal costo; 
    private LocalDateTime fechaIngreso; 
    private Long idCategoria;
    private String nombreCategoria; 
}