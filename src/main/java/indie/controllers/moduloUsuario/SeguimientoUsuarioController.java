package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.dtos.moduloUsuario.EstadisticasSeguimientoDTO;
import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.security.JwtUtils;
import indie.services.moduloUsuario.SeguimientoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seguimiento")
@CrossOrigin(origins = "*")
public class SeguimientoUsuarioController extends BaseController<SeguimientoUsuario, String> {

    @Autowired
    private SeguimientoUsuarioService seguimientoUsuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JwtUtils jwtUtils;

    public SeguimientoUsuarioController(SeguimientoUsuarioService seguimientoUsuarioService) {
        super(seguimientoUsuarioService);
        this.seguimientoUsuarioService = seguimientoUsuarioService;
    }

    /**
     * Método helper para obtener el username del usuario autenticado
     */
    private String obtenerUsernameDesdeToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String usuarioActualId = jwtUtils.extraerIdFromToken(token);
        Usuario usuarioActual = usuarioRepository.findById(usuarioActualId)
                .orElseThrow(() -> new RuntimeException("Usuario actual no encontrado"));
        return usuarioActual.getUsername();
    }

    /**
     * Método helper para obtener el ID de usuario desde username
     */
    private String obtenerIdDesdeUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }
        return usuario.getId();
    }

 
    @PostMapping("/seguir/{username}")
    public ResponseEntity<?> seguirUsuario(@PathVariable String username, 
                                         @RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            String usuarioASegeguirId = obtenerIdDesdeUsername(username);
            
            SeguimientoUsuario resultado = seguimientoUsuarioService.seguirUsuario(usuarioActualId, usuarioASegeguirId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ahora sigues a este usuario");
            response.put("seguimiento", resultado);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

   
    @DeleteMapping("/dejarDeSeguir/{username}")
    public ResponseEntity<?> dejarDeSeguirUsuario(@PathVariable String username, 
                                                @RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            String usuarioADejarDeSeguirId = obtenerIdDesdeUsername(username);
            
            seguimientoUsuarioService.dejarDeSeguirUsuario(usuarioActualId, usuarioADejarDeSeguirId);
            
            return ResponseEntity.ok(Map.of("mensaje", "Has dejado de seguir a este usuario"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Bloquear a un usuario
     */
    @PostMapping("/bloquear/{username}")
    public ResponseEntity<?> bloquearUsuario(@PathVariable String username, 
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            String usuarioABloquearId = obtenerIdDesdeUsername(username);
            
            SeguimientoUsuario resultado = seguimientoUsuarioService.bloquearUsuario(usuarioActualId, usuarioABloquearId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario bloqueado exitosamente");
            response.put("bloqueo", resultado);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

  
    @DeleteMapping("/desbloquear/{username}")
    public ResponseEntity<?> desbloquearUsuario(@PathVariable String username, 
                                              @RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            String usuarioADesbloquearId = obtenerIdDesdeUsername(username);
            
            seguimientoUsuarioService.desbloquearUsuario(usuarioActualId, usuarioADesbloquearId);
            
            return ResponseEntity.ok(Map.of("mensaje", "Usuario desbloqueado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }


    @GetMapping("/seguidores/{username}")
    public ResponseEntity<?> traerSeguidores(@PathVariable String username) {
        try {
            String usuarioId = obtenerIdDesdeUsername(username);
            
            List<Usuario> seguidores = seguimientoUsuarioService.traerSeguidores(usuarioId);
            long totalSeguidores = seguimientoUsuarioService.contarSeguidores(usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("seguidores", seguidores);
            response.put("totalSeguidores", totalSeguidores);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

 
    @GetMapping("/seguidos/{username}")
    public ResponseEntity<?> traerSeguidos(@PathVariable String username) {
        try {
            String usuarioId = obtenerIdDesdeUsername(username);
            
            List<Usuario> seguidos = seguimientoUsuarioService.traerSeguidos(usuarioId);
            long totalSeguidos = seguimientoUsuarioService.contarSeguidos(usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("seguidos", seguidos);
            response.put("totalSeguidos", totalSeguidos);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

  
    @GetMapping("/misSeguidores")
    public ResponseEntity<?> traerMisSeguidores(@RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            
            List<Usuario> seguidores = seguimientoUsuarioService.traerSeguidores(usuarioActualId);
            long totalSeguidores = seguimientoUsuarioService.contarSeguidores(usuarioActualId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("seguidores", seguidores);
            response.put("totalSeguidores", totalSeguidores);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    
    @GetMapping("/misSeguidos")
    public ResponseEntity<?> traerMisSeguidos(@RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            
            List<Usuario> seguidos = seguimientoUsuarioService.traerSeguidos(usuarioActualId);
            long totalSeguidos = seguimientoUsuarioService.contarSeguidos(usuarioActualId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("seguidos", seguidos);
            response.put("totalSeguidos", totalSeguidos);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    
    @GetMapping("/bloqueados")
    public ResponseEntity<?> traerUsuariosBloqueados(@RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            
            List<Usuario> bloqueados = seguimientoUsuarioService.traerUsuariosBloqueados(usuarioActualId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("usuariosBloqueados", bloqueados);
            response.put("totalBloqueados", bloqueados.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

   
    @GetMapping("/verificar-seguimiento/{username}")
    public ResponseEntity<?> verificarSeguimiento(@PathVariable String username, 
                                                @RequestHeader("Authorization") String authHeader) {
        try {
            String usuarioActualUsername = obtenerUsernameDesdeToken(authHeader);
            String usuarioActualId = obtenerIdDesdeUsername(usuarioActualUsername);
            String usuarioObjetivoId = obtenerIdDesdeUsername(username);
            
            boolean sigue = seguimientoUsuarioService.verificarSiSigue(usuarioActualId, usuarioObjetivoId);
            boolean bloqueado = seguimientoUsuarioService.verificarSiEstaBloqueado(usuarioActualId, usuarioObjetivoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("sigue", sigue);
            response.put("bloqueado", bloqueado);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @GetMapping("/estadisticas/{username}")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable String username) {
        try {
            String usuarioId = obtenerIdDesdeUsername(username);
            
            long totalSeguidores = seguimientoUsuarioService.contarSeguidores(usuarioId);
            long totalSeguidos = seguimientoUsuarioService.contarSeguidos(usuarioId);
            
            EstadisticasSeguimientoDTO estadisticas = EstadisticasSeguimientoDTO.builder()
                    .totalSeguidores(totalSeguidores)
                    .totalSeguidos(totalSeguidos)
                    .build();
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
