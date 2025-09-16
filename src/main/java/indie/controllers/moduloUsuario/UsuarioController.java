package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.dtos.auth.ChangePasswordRequest;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.UsuarioService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController extends BaseController<Usuario, String> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    @GetMapping("/por-username")
    public ResponseEntity<?> getPorUsername(@RequestParam String username) {
        var opt = usuarioService.findByUsername(username);

        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal String email) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
    }

    @PutMapping("/update-me")
    public ResponseEntity<?> updateMe(@AuthenticationPrincipal String email, @RequestBody Usuario usuario) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isPresent()) {
            Usuario usuarioExistente = opt.get();
            if (usuario.getNombreUsuario() != null) {
                usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
            }

            Usuario usuarioActualizado = usuarioService.save(usuarioExistente);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            String mensaje = usuarioService.cambiarPassword(
                    request.getEmail(),
                    request.getCurrentPassword(),
                    request.getNewPassword());
            return ResponseEntity.ok(mensaje);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
