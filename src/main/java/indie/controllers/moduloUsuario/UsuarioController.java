package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.dtos.auth.ChangePasswordRequest;
import indie.models.moduloUsuario.Usuario;
import indie.security.JwtUtils;
import indie.services.moduloUsuario.SeguimientoUsuarioService;
import indie.services.moduloUsuario.UsuarioService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController extends BaseController<Usuario, String> {

    private final UsuarioService usuarioService;
    
    @Autowired
    private SeguimientoUsuarioService seguimientoUsuarioService;
    
    @Autowired
    private JwtUtils jwtUtils;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService); 
        this.usuarioService = usuarioService;
    }

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

    @GetMapping("/por-username")
    public ResponseEntity<?> getPorUsername(@RequestParam("username") String username,
                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            var opt = usuarioService.findByUsername(username);

            if (!opt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }

            Usuario usuarioSolicitado = opt.get();

            // Si hay un usuario autenticado, verificar si está siendo bloqueado
            if (authHeader != null) {
                String usuarioActualId = obtenerIdUsuarioDesdeToken(authHeader);
                
                if (usuarioActualId != null) {
                    // Verificar si el usuario solicitado tiene bloqueado al usuario actual
                    boolean estaSiendoBloqueado = seguimientoUsuarioService
                            .verificarSiEstaSiendoBloqueado(usuarioActualId, usuarioSolicitado.getId());
                    
                    if (estaSiendoBloqueado) {
                        return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(Map.of(
                                    "error", "No tienes permisos para ver este perfil",
                                    "bloqueado", true
                                ));
                    }
                }
            }

            return ResponseEntity.ok(usuarioSolicitado);
            
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
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
