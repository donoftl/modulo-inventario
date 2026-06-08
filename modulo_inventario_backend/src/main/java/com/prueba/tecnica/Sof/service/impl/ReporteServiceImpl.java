package com.prueba.tecnica.Sof.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.prueba.tecnica.Sof.dto.ReporteResponseDTO;
import com.prueba.tecnica.Sof.dto.repository.ActivoRepository;
import com.prueba.tecnica.Sof.extras.ActivoSearchCriteria;
import com.prueba.tecnica.Sof.model.Activo;
import com.prueba.tecnica.Sof.service.ReporteService;

// ¡IMPORTANTE! Estos son los dos imports clave para los filtros de base de datos
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ActivoRepository activoRepository;

    @Override
    public ReporteResponseDTO generarReporteZip(ActivoSearchCriteria criteria, String usuarioSolicitante) {
        // 1. Obtener la lista de activos aplicando los mismos filtros de búsqueda del tablero
        List<Activo> activos = activoRepository.findAll(crearSpecification(criteria));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            // 2. Generar y añadir el archivo Excel (.xlsx) al ZIP
            byte[] excelBytes = generarExcelBytes(activos);
            ZipEntry excelEntry = new ZipEntry("reporte_activos.xlsx");
            zos.putNextEntry(excelEntry);
            zos.write(excelBytes);
            zos.closeEntry();

            // 3. Generar y añadir el archivo plano de Auditoría (.txt) al ZIP
            byte[] auditoriaBytes = generarAuditoriaBytes(activos.size(), usuarioSolicitante);
            ZipEntry txtEntry = new ZipEntry("auditoria.txt");
            zos.putNextEntry(txtEntry);
            zos.write(auditoriaBytes);
            zos.closeEntry();

            // Asegurar el cierre correcto del flujo de compresión antes de la conversión
            zos.finish();

            // 4. Convertir el ZIP final generado en memoria a Base64
            String base64Resultado = Base64.getEncoder().encodeToString(baos.toByteArray());

            return new ReporteResponseDTO(
                    200,
                    "Reporte generado correctamente",
                    "inventario.zip",
                    base64Resultado
            );

        } catch (IOException e) {
            throw new RuntimeException("Error fatal al generar el archivo comprimido ZIP", e);
        }
    }

    // Generación de la estructura del documento Excel en memoria (Apache POI)
    private byte[] generarExcelBytes(List<Activo> activos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Activos Tecnológicos");

            // Estilo visual básico para resaltar los encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Crear fila de encabezados principales
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"UUID", "Folio", "Número de Serie", "Marca/Modelo", "Estado", "Costo", "Fecha Ingreso", "Categoría"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Inyectar los datos de cada activo recuperado
            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Activo activo : activos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(activo.getId().toString());
                row.createCell(1).setCellValue(activo.getFolio());
                row.createCell(2).setCellValue(activo.getNumeroSerie());
                row.createCell(3).setCellValue(activo.getMarcaModelo());
                row.createCell(4).setCellValue(activo.getEstado());
                row.createCell(5).setCellValue(activo.getCosto().doubleValue());
                row.createCell(6).setCellValue(activo.getFechaIngreso().format(formatter));
                row.createCell(7).setCellValue(activo.getCategoria().getNombre());
            }

            // Autoajustar el ancho de las columnas según su contenido
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Generación del archivo de auditoría plana solicitado en los requerimientos
    private byte[] generarAuditoriaBytes(int totalRegistros, String usuario) {
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String contenido = "========================================\n" +
                           "      REPORTES DE AUDITORÍA INTERNA     \n" +
                           "========================================\n" +
                           "Fecha y hora de generación : " + fechaHora + "\n" +
                           "Usuario solicitante        : " + usuario + "\n" +
                           "Total de registros exportados: " + totalRegistros + "\n" +
                           "========================================\n";

        return contenido.getBytes(StandardCharsets.UTF_8);
    }

    // Motor de especificaciones dinámicas para aplicar los mismos filtros aplicados en el tablero principal
    private Specification<Activo> crearSpecification(ActivoSearchCriteria criteria) {
        return (Root<Activo> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
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
    }
}