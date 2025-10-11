package indie.controllers.moduloCalendario;

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

}
