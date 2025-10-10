package indie.controllers.moduloUsuario;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.UsuarioUbicacion;
import indie.services.moduloUsuario.UsuarioService;
import indie.services.moduloUsuario.UsuarioUbicacionService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/ubicaciones")
public class UsuarioUbicacionController {

    private final UsuarioUbicacionService ubicacionService;
    private final UsuarioService usuarioService;

    public UsuarioUbicacionController(UsuarioUbicacionService ubicacionService, UsuarioService usuarioService) {
        this.ubicacionService = ubicacionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<?> listarPropias(@AuthenticationPrincipal String email) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Usuario u = opt.get();
        List<UsuarioUbicacion> list = ubicacionService.findByUsuarioId(u.getId());
        return ResponseEntity.ok(list);
    }

    // Listar ubicaciones por id de usuario (para ver en perfiles ajenos)
    @GetMapping("/por-usuario/{userId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable String userId) {
        List<UsuarioUbicacion> list = ubicacionService.findByUsuarioId(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> crear(@AuthenticationPrincipal String email, @RequestBody UsuarioUbicacion body) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Usuario u = opt.get();
        body.setId(null);
        body.setUsuario(u);
        UsuarioUbicacion saved = ubicacionService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@AuthenticationPrincipal String email, @PathVariable String id, @RequestBody UsuarioUbicacion body) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Usuario u = opt.get();
        UsuarioUbicacion existente = ubicacionService.findById(id);
        if (!existente.getUsuario().getId().equals(u.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No autorizado"));
        }
        existente.setDireccion(body.getDireccion());
        existente.setLatitud(body.getLatitud());
        existente.setLongitud(body.getLongitud());
        UsuarioUbicacion actualizado = ubicacionService.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@AuthenticationPrincipal String email, @PathVariable String id) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Usuario u = opt.get();
        UsuarioUbicacion existente = ubicacionService.findById(id);
        if (!existente.getUsuario().getId().equals(u.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No autorizado"));
        }
        ubicacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
