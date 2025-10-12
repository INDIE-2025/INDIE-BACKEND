package indie.controllers.moduloCalendario;

import indie.dtos.moduloCalendario.CalendarioDTO;
import indie.models.moduloCalendario.Calendario;
import indie.services.moduloCalendario.CalendarioServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioController {

    private final CalendarioServiceImpl calendarioService;

    public CalendarioController(CalendarioServiceImpl calendarioServiceImpl) {
        this.calendarioService = calendarioServiceImpl;
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<Calendario> obtenerCalendarioPorUsername(@PathVariable String username) {
        try {
            return calendarioService.findByUsername(username)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    
    @GetMapping("/usuario/id/{usuarioId}")
    public ResponseEntity<Calendario> obtenerCalendarioPorUsuarioId(@PathVariable String usuarioId) {
        try {
            return calendarioService.findByUsuarioId(usuarioId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // === NUEVOS ENDPOINTS CON DTO (SIN REFERENCIAS CIRCULARES) ===

    @GetMapping("/dto/usuario/{username}")
    public ResponseEntity<CalendarioDTO> obtenerCalendarioDTOPorUsername(@PathVariable String username) {
        try {
            return calendarioService.findDTOByUsername(username)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dto/usuario/id/{usuarioId}")
    public ResponseEntity<CalendarioDTO> obtenerCalendarioDTOPorUsuarioId(@PathVariable String usuarioId) {
        try {
            return calendarioService.findDTOByUsuarioId(usuarioId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dto/{calendarioId}")
    public ResponseEntity<CalendarioDTO> obtenerCalendarioDTOPorCalendarioId(@PathVariable String calendarioId) {
        try {
            return calendarioService.findDTOByCalendarioId(calendarioId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
