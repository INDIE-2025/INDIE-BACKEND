package indie.controllers.moduloReportes;

import indie.dtos.moduloReportes.ReporteUsuarioDTO;
import indie.dtos.moduloReportes.SolicitudReporteDTO;
import indie.dtos.moduloReportes.ReporteExportacionDTO;
import indie.security.JwtUtils;
import indie.services.moduloReportes.ReporteUsuarioService;
import indie.services.moduloReportes.ExportacionReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes", description = "Endpoints para generar reportes de métricas de usuarios")
public class ReporteUsuarioController {
    
    private final ReporteUsuarioService reporteUsuarioService;
    private final ExportacionReporteService exportacionReporteService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * Método helper para obtener el ID del usuario autenticado desde el token
     */
    private String obtenerIdUsuarioDesdeToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.replace("Bearer ", "");
        return jwtUtils.extraerIdFromToken(token);
    }
    
    /**
     * Obtiene la dirección IP real del cliente considerando proxies
     */
    private String obtenerDireccionIpReal(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    @PostMapping("/usuario/{usuarioId}")
    @Operation(
        summary = "Generar reporte de usuario",
        description = "Genera un reporte completo de métricas para un usuario específico en el período solicitado"
    )
    public ResponseEntity<ReporteUsuarioDTO> generarReporteUsuario(
            @Parameter(description = "ID del usuario") @PathVariable String usuarioId,
            @Valid @RequestBody SolicitudReporteDTO solicitud,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ReporteUsuarioDTO reporte = reporteUsuarioService.generarReporteUsuario(
            usuarioId, 
            solicitud.getFechaInicio(), 
            solicitud.getFechaFin()
        );
        
        return ResponseEntity.ok(reporte);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(
        summary = "Generar reporte simple de usuario",
        description = "Genera un reporte de métricas con parámetros de consulta"
    )
    public ResponseEntity<ReporteUsuarioDTO> generarReporteSimple(
            @Parameter(description = "ID del usuario") @PathVariable String usuarioId,
            @Parameter(description = "Fecha de inicio") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @Parameter(description = "Incluir gráficos") @RequestParam(defaultValue = "true") boolean incluirGraficos,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ReporteUsuarioDTO reporte = reporteUsuarioService.generarReporteUsuario(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
    
    @PostMapping("/visualizacion-perfil/{usuarioVisitadoId}")
    @Operation(
        summary = "Registrar visualización de perfil",
        description = "Registra cuando un usuario visualiza el perfil de otro usuario"
    )
    public ResponseEntity<Void> registrarVisualizacionPerfil(
            @Parameter(description = "ID del usuario visitado") @PathVariable String usuarioVisitadoId,
            HttpServletRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String direccionIp = obtenerDireccionIpReal(request);
        String userAgent = request.getHeader("User-Agent");
        String usuarioVisitanteId = null;
        
        // Si hay un usuario autenticado, obtener su ID
        if (authHeader != null) {
            usuarioVisitanteId = obtenerIdUsuarioDesdeToken(authHeader);
        }
        
        reporteUsuarioService.registrarVisualizacionPerfil(
            usuarioVisitadoId, 
            usuarioVisitanteId, 
            direccionIp, 
            userAgent
        );
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/usuario/{usuarioId}/ultimo-mes")
    @Operation(
        summary = "Reporte del último mes",
        description = "Genera un reporte de métricas del último mes"
    )
    public ResponseEntity<ReporteUsuarioDTO> generarReporteUltimoMes(
            @Parameter(description = "ID del usuario") @PathVariable String usuarioId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusMonths(1);
        
        ReporteUsuarioDTO reporte = reporteUsuarioService.generarReporteUsuario(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
    
    @GetMapping("/usuario/{usuarioId}/ultimos-7-dias")
    @Operation(
        summary = "Reporte de los últimos 7 días",
        description = "Genera un reporte de métricas de los últimos 7 días"
    )
    public ResponseEntity<ReporteUsuarioDTO> generarReporteUltimos7Dias(
            @Parameter(description = "ID del usuario") @PathVariable String usuarioId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusDays(7);
        
        ReporteUsuarioDTO reporte = reporteUsuarioService.generarReporteUsuario(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

    // ==================== ENDPOINTS DE EXPORTACIÓN ====================

    @GetMapping("/usuario/{usuarioId}/exportar/excel")
    @Operation(summary = "Exportar reporte de usuario a Excel", 
               description = "Exporta las métricas del usuario en formato Excel")
    public ResponseEntity<byte[]> exportarReporteExcel(
            @PathVariable String usuarioId,
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestHeader("Authorization") String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Fechas por defecto: últimos 30 días
        if (fechaFin == null) fechaFin = LocalDate.now();
        if (fechaInicio == null) fechaInicio = fechaFin.minusDays(30);
        
        List<ReporteExportacionDTO> reportes = reporteUsuarioService.obtenerReportesDetallados(usuarioId, fechaInicio, fechaFin);
        byte[] excelContent = exportacionReporteService.exportarExcel(reportes);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
            "attachment; filename=reporte_" + usuarioId + "_" + fechaInicio + "_" + fechaFin + ".xlsx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelContent);
    }

    @GetMapping("/usuario/{usuarioId}/exportar/pdf")
    @Operation(summary = "Exportar reporte de usuario a PDF", 
               description = "Exporta las métricas del usuario en formato PDF")
    public ResponseEntity<byte[]> exportarReportePDF(
            @PathVariable String usuarioId,
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestHeader("Authorization") String authHeader) {
        
        String usuarioAutenticadoId = obtenerIdUsuarioDesdeToken(authHeader);
        if (usuarioAutenticadoId == null || !usuarioAutenticadoId.equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Fechas por defecto: últimos 30 días
        if (fechaFin == null) fechaFin = LocalDate.now();
        if (fechaInicio == null) fechaInicio = fechaFin.minusDays(30);
        
        List<ReporteExportacionDTO> reportes = reporteUsuarioService.obtenerReportesDetallados(usuarioId, fechaInicio, fechaFin);
        byte[] pdfContent = exportacionReporteService.exportarPDF(reportes);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
            "attachment; filename=reporte_" + usuarioId + "_" + fechaInicio + "_" + fechaFin + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfContent);
    }
}