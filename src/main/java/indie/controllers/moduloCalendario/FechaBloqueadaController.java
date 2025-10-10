package indie.controllers.moduloCalendario;

import indie.dtos.moduloCalendario.ActualizarFechaBloqueadaDTO;
import indie.dtos.moduloCalendario.BloquearFechasDTO;
import indie.dtos.moduloCalendario.FechaBloqueadaResponseDTO;
import indie.services.moduloCalendario.CalendarioServiceImpl;
import indie.services.moduloCalendario.FechaBloqueadaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendario")
public class FechaBloqueadaController {
    
    private final FechaBloqueadaService fechaBloqueadaService;
    private final CalendarioServiceImpl calendarioService;
    
    public FechaBloqueadaController(FechaBloqueadaService fechaBloqueadaService,
                                   CalendarioServiceImpl calendarioService) {
        this.fechaBloqueadaService = fechaBloqueadaService;
        this.calendarioService = calendarioService;
    }
    
    /**
     * Bloquear un rango de fechas
     * POST /api/calendario/fechas-bloqueadas
     */
    @PostMapping("/fechas-bloqueadas")
    public ResponseEntity<FechaBloqueadaResponseDTO> bloquearFechas(
            @Valid @RequestBody BloquearFechasDTO dto) {
        try {
            FechaBloqueadaResponseDTO response = fechaBloqueadaService.bloquearFechas(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener todas las fechas bloqueadas de un calendario
     * GET /api/calendario/fechas-bloqueadas/calendario/{calendarioId}
     */
    @GetMapping("/calendario/{calendarioId}")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadas(
            @PathVariable("calendarioId") String calendarioId) {
        try {
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.obtenerFechasBloqueadas(calendarioId);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener fechas bloqueadas en un rango específico
     * GET /api/calendario/fechas-bloqueadas/calendario/{calendarioId}/rango
     */
    @GetMapping("/calendario/{calendarioId}/rango")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadasEnRango(
            @PathVariable("calendarioId") String calendarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService
                    .obtenerFechasBloqueadasEnRango(calendarioId, fechaInicio, fechaFin);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Verificar si una fecha está bloqueada
     * GET /api/calendario/fechas-bloqueadas/calendario/{calendarioId}/verificar
     */
    @GetMapping("/calendario/{calendarioId}/verificar")
    public ResponseEntity<Boolean> verificarFechaBloqueada(
            @PathVariable("calendarioId") String calendarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        try {
            boolean bloqueada;
            if (hora != null) {
                bloqueada = fechaBloqueadaService.isFechaHoraBloqueada(calendarioId, fecha, hora);
            } else {
                bloqueada = fechaBloqueadaService.isFechaBloqueada(calendarioId, fecha);
            }
            return ResponseEntity.ok(bloqueada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener una fecha bloqueada por ID
     * GET /api/calendario/fechas-bloqueadas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FechaBloqueadaResponseDTO> obtenerFechaBloqueadaPorId(@PathVariable("id") String id) {
        try {
            FechaBloqueadaResponseDTO fecha = fechaBloqueadaService.obtenerFechaBloqueadaPorId(id);
            return ResponseEntity.ok(fecha);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Actualizar una fecha bloqueada
     * PUT /api/calendario/fechas-bloqueadas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<FechaBloqueadaResponseDTO> actualizarFechaBloqueada(
            @PathVariable("id") String id,
            @Valid @RequestBody ActualizarFechaBloqueadaDTO dto) {
        try {
            FechaBloqueadaResponseDTO fechaActualizada = fechaBloqueadaService.actualizarFechaBloqueada(id, dto);
            return ResponseEntity.ok(fechaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar (desactivar) una fecha bloqueada
     * DELETE /api/calendario/fechas-bloqueadas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFechaBloqueada(@PathVariable String id) {
        try {
            fechaBloqueadaService.eliminarFechaBloqueada(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Buscar fechas bloqueadas por motivo
     * GET /api/calendario/fechas-bloqueadas/calendario/{calendarioId}/buscar
     */
    @GetMapping("/calendario/{calendarioId}/buscar")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> buscarPorMotivo(
            @PathVariable String calendarioId,
            @RequestParam String motivo) {
        try {
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.buscarPorMotivo(calendarioId, motivo);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener fechas bloqueadas futuras
     * GET /api/calendario/fechas-bloqueadas/calendario/{calendarioId}/futuras
     */
    @GetMapping("/calendario/{calendarioId}/futuras")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadasFuturas(
            @PathVariable String calendarioId) {
        try {
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.obtenerFechasBloqueadasFuturas(calendarioId);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // ============ ENDPOINTS BASADOS EN USERNAME (RECOMENDADOS) ============
    
    /**
     * Obtener todas las fechas bloqueadas de un usuario por username
     * GET /api/calendario/usuario/{username}/fechas-bloqueadas
     */
    @GetMapping("/usuario/{username}/fechas-bloqueadas")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadasPorUsername(
            @PathVariable("username") String username) {
        try {
            var calendarioOpt = calendarioService.findByUsername(username);
            if (calendarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.obtenerFechasBloqueadas(calendarioOpt.get().getId());
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener fechas bloqueadas en un rango específico por username
     * GET /api/calendario/usuario/{username}/fechas-bloqueadas/rango
     */
    @GetMapping("/usuario/{username}/fechas-bloqueadas/rango")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadasEnRangoPorUsername(
            @PathVariable("username") String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            var calendarioOpt = calendarioService.findByUsername(username);
            if (calendarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService
                    .obtenerFechasBloqueadasEnRango(calendarioOpt.get().getId(), fechaInicio, fechaFin);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Verificar si una fecha está bloqueada por username
     * GET /api/calendario/usuario/{username}/fechas-bloqueadas/verificar
     */
    @GetMapping("/usuario/{username}/fechas-bloqueadas/verificar")
    public ResponseEntity<Boolean> verificarFechaBloqueadaPorUsername(
            @PathVariable("username") String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        try {
            var calendarioOpt = calendarioService.findByUsername(username);
            if (calendarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            boolean bloqueada;
            if (hora != null) {
                bloqueada = fechaBloqueadaService.isFechaHoraBloqueada(calendarioOpt.get().getId(), fecha, hora);
            } else {
                bloqueada = fechaBloqueadaService.isFechaBloqueada(calendarioOpt.get().getId(), fecha);
            }
            return ResponseEntity.ok(bloqueada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Buscar fechas bloqueadas por motivo por username
     * GET /api/calendario/usuario/{username}/fechas-bloqueadas/buscar
     */
    @GetMapping("/usuario/{username}/fechas-bloqueadas/buscar")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> buscarPorMotivoPorUsername(
            @PathVariable("username") String username,
            @RequestParam String motivo) {
        try {
            var calendarioOpt = calendarioService.findByUsername(username);
            if (calendarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.buscarPorMotivo(calendarioOpt.get().getId(), motivo);
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener fechas bloqueadas futuras por username
     * GET /api/calendario/usuario/{username}/fechas-bloqueadas/futuras
     */
    @GetMapping("/usuario/{username}/fechas-bloqueadas/futuras")
    public ResponseEntity<List<FechaBloqueadaResponseDTO>> obtenerFechasBloqueadasFuturasPorUsername(
            @PathVariable("username") String username) {
        try {
            var calendarioOpt = calendarioService.findByUsername(username);
            if (calendarioOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<FechaBloqueadaResponseDTO> fechas = fechaBloqueadaService.obtenerFechasBloqueadasFuturas(calendarioOpt.get().getId());
            return ResponseEntity.ok(fechas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
