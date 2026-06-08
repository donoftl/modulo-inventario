package com.prueba.tecnica.Sof.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteResponseDTO {
    private int status;
    private String message;
    private String fileName;
    private String fileBase64;
}