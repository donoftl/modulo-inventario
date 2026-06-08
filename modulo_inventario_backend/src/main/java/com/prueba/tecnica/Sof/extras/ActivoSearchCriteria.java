package com.prueba.tecnica.Sof.extras;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivoSearchCriteria {
    private String numeroSerie; 
    private String marcaModelo; 
    private Long idCategoria; 
    private String estado;
    private BigDecimal costoMin; 
    private BigDecimal costoMax; 
}

