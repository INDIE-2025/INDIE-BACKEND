package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.dtos.GenericResponseDTO;
import indie.models.moduloEventos.Interes;
import indie.services.moduloEventos.InteresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/eventos/interes")
public class InteresController extends BaseController<Interes, String> {

    @Autowired
    protected InteresService interesService;

    public InteresController(InteresService interesService) {
        super(interesService);
        this.interesService = interesService;
    }

    @PostMapping("/{idEvento}/marcar")
    public ResponseEntity<GenericResponseDTO> marcarInteres(
            @PathVariable("idEvento") String idEvento,
            @RequestHeader("X-User-Id") String idUsuario) {

        try {
            boolean resultado = interesService.marcarInteresEnEvento(idEvento, idUsuario);
            
            Map<String, Object> data = new HashMap<>();
            data.put("marcado", resultado);
            data.put("cantidadInteresados", interesService.contarInteresadosPorEvento(idEvento));
            
            return ResponseEntity.ok(
                    GenericResponseDTO.builder()
                            .success(true)
                            .message(resultado ? "Interés marcado correctamente" : "Ya estabas interesado en este evento")
                            .data(data)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message("Error al marcar interés: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/{idEvento}/quitar")
    public ResponseEntity<GenericResponseDTO> quitarInteres(
            @PathVariable("idEvento") String idEvento,
            @RequestHeader("X-User-Id") String idUsuario) {

        try {
            boolean resultado = interesService.quitarInteresEnEvento(idEvento, idUsuario);
            
            Map<String, Object> data = new HashMap<>();
            data.put("quitado", resultado);
            data.put("cantidadInteresados", interesService.contarInteresadosPorEvento(idEvento));
            
            return ResponseEntity.ok(
                    GenericResponseDTO.builder()
                            .success(true)
                            .message(resultado ? "Interés quitado correctamente" : "No estabas interesado en este evento")
                            .data(data)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message("Error al quitar interés: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{idEvento}/estado")
    public ResponseEntity<GenericResponseDTO> consultarEstado(
            @PathVariable("idEvento") String idEvento,
            @RequestHeader("X-User-Id") String idUsuario) {

        try {
            boolean estaInteresado = interesService.estaInteresado(idEvento, idUsuario);
            int cantidadInteresados = interesService.contarInteresadosPorEvento(idEvento);
            
            Map<String, Object> data = new HashMap<>();
            data.put("interesado", estaInteresado);
            data.put("cantidadInteresados", cantidadInteresados);
            
            return ResponseEntity.ok(
                    GenericResponseDTO.builder()
                            .success(true)
                            .data(data)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message("Error al consultar estado de interés: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{idEvento}/cantidad")
    public ResponseEntity<GenericResponseDTO> contarInteresados(@PathVariable("idEvento") String idEvento) {
        try {
            int cantidad = interesService.contarInteresadosPorEvento(idEvento);
            
            Map<String, Object> data = new HashMap<>();
            data.put("cantidadInteresados", cantidad);
            
            return ResponseEntity.ok(
                    GenericResponseDTO.builder()
                            .success(true)
                            .data(data)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponseDTO.builder()
                            .success(false)
                            .message("Error al contar interesados: " + e.getMessage())
                            .build()
            );
        }
    }
}
