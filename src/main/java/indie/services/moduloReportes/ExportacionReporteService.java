package indie.services.moduloReportes;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import indie.dtos.moduloReportes.ReporteExportacionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ExportacionReporteService {

    /**
     * Exporta reportes a formato Excel
     */
    public byte[] exportarExcel(List<ReporteExportacionDTO> reportes) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Métricas");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] encabezados = {
                "Fecha", "Métrica", "Valor", "Unidad", "Descripción"
            };

            for (int i = 0; i < encabezados.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(encabezados[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (ReporteExportacionDTO reporte : reportes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reporte.getFecha().toString());
                row.createCell(1).setCellValue(reporte.getNombreMetrica());
                row.createCell(2).setCellValue(reporte.getValorMetrica().doubleValue());
                row.createCell(3).setCellValue(reporte.getUnidadMedida());
                row.createCell(4).setCellValue(reporte.getDescripcionMetrica());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < encabezados.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error al exportar a Excel: {}", e.getMessage());
            throw new RuntimeException("Error al generar archivo Excel", e);
        }
    }

    /**
     * Exporta reportes a formato PDF
     */
    public byte[] exportarPDF(List<ReporteExportacionDTO> reportes) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            document.add(new Paragraph("Reporte de Métricas de Usuario")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

            // Fecha de generación
            document.add(new Paragraph("Generado el: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // Tabla
            Table table = new Table(5);
            table.setWidth(100);

            // Encabezados
            table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Métrica").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Valor").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Unidad").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Descripción").setBold()));

            // Datos
            for (ReporteExportacionDTO reporte : reportes) {
                table.addCell(new Cell().add(new Paragraph(reporte.getFecha().toString())));
                table.addCell(new Cell().add(new Paragraph(reporte.getNombreMetrica())));
                table.addCell(new Cell().add(new Paragraph(reporte.getValorMetrica().toString())));
                table.addCell(new Cell().add(new Paragraph(reporte.getUnidadMedida())));
                table.addCell(new Cell().add(new Paragraph(reporte.getDescripcionMetrica())));
            }

            document.add(table);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error al exportar a PDF: {}", e.getMessage());
            throw new RuntimeException("Error al generar archivo PDF", e);
        }
    }
}