package com.prueba.tecnica.Sof.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ActivoRequestDTO {

    @NotBlank(message = "El número de serie es obligatorio") 
    @Size(max = 100, message = "El número de serie no puede exceder los 100 caracteres") 
    private String numeroSerie;

    @NotBlank(message = "La marca y modelo son obligatorios")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres") 
    private String marcaModelo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El costo de adquisición es obligatorio")
    @DecimalMin(value = "0.0", message = "El costo no puede ser negativo") 
    private BigDecimal costo;

    @NotNull(message = "La categoría es obligatoria") 
    private Long idCategoria;
}
