package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.dtos.auth.ChangePasswordRequest;
import indie.models.moduloUsuario.Usuario;
import indie.security.JwtUtils;
import indie.services.moduloUsuario.SeguimientoUsuarioService;
import indie.services.moduloUsuario.UsuarioService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    // ========================= Foto de Perfil =========================

    @PostMapping(value = "/foto-perfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFotoPerfil(
            @AuthenticationPrincipal String email,
            @RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Archivo vacio"));
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten imagenes"));
            }
            // Limite simple de tamano (5MB)
            if (file.getSize() > 5L * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(Map.of("error", "La imagen excede 5MB"));
            }

            var opt = usuarioService.buscarPorEmail(email);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            Usuario u = opt.get();
            u.setFotoPerfil(file.getBytes());
            u.setFotoPerfilContentType(contentType);
            usuarioService.actualizarUsuario(u);
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo subir la foto"));
        }
    }

    @GetMapping(value = "/me/foto-perfil")
    public ResponseEntity<byte[]> obtenerMiFotoPerfil(@AuthenticationPrincipal String email) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty() || opt.get().getFotoPerfil() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Usuario u = opt.get();
        String ct = (u.getFotoPerfilContentType() != null && !u.getFotoPerfilContentType().isBlank())
                ? u.getFotoPerfilContentType() : MediaType.IMAGE_JPEG_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ct))
                .body(u.getFotoPerfil());
    }

    @GetMapping(value = "/{id}/foto-perfil")
    public ResponseEntity<byte[]> obtenerFotoPerfilPorId(@PathVariable("id") String id) {
        try {
            var u = usuarioService.findById(id);
            if (u == null || u.getFotoPerfil() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            String ct = (u.getFotoPerfilContentType() != null && !u.getFotoPerfilContentType().isBlank())
                    ? u.getFotoPerfilContentType() : MediaType.IMAGE_JPEG_VALUE;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(ct))
                    .body(u.getFotoPerfil());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/me/tiene-foto")
    public ResponseEntity<Boolean> tieneFotoPerfil(@AuthenticationPrincipal String email) {
        var opt = usuarioService.buscarPorEmail(email);
        boolean has = opt.isPresent() && opt.get().getFotoPerfil() != null;
        return ResponseEntity.ok(has);
    }

    @DeleteMapping("/foto-perfil")
    public ResponseEntity<?> eliminarFotoPerfil(@AuthenticationPrincipal String email) {
        var opt = usuarioService.buscarPorEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
        Usuario u = opt.get();
        u.setFotoPerfil(null);
        u.setFotoPerfilContentType(null);
        usuarioService.actualizarUsuario(u);
        return ResponseEntity.noContent().build();
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
            usuario.setUsername(usuarioExistente.getUsername()); // No permitir cambiar el username
            usuario.setEmailUsuario(usuarioExistente.getEmailUsuario()); // No permitir cambiar el email
            usuario.setPassword(usuarioExistente.getPassword()); // No permitir cambiar el password aquí

            if (usuario.getNombreUsuario() != null) {
                usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
            }
            Usuario usuarioActualizado = usuarioService.save(usuario);
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
    
    /**
     * Sobrescribe el método getById del controlador base para manejar los errores adecuadamente
     * @param id ID del usuario a buscar
     * @return Usuario encontrado o error 404 si no existe
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") String id) {
        System.out.println("Buscando usuario con ID: " + id);
        try {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            System.out.println("Error al buscar usuario por ID: " + e.getMessage());
            throw new RuntimeException("Usuario no encontrado con ID: " + id, e);
        }
    }
    
    /**
     * Endpoint para buscar usuarios por nombre de usuario parcial, ignorando mayúsculas/minúsculas
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarios(@RequestParam(name = "username") String username) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Recibida búsqueda de usuarios con término: [" + username + "]");
        
        try {
            if (username == null || username.trim().isEmpty()) {
                System.out.println("Término de búsqueda vacío o nulo");
                return ResponseEntity.badRequest().body(Map.of("error", "El término de búsqueda no puede estar vacío"));
            }
            
            // Eliminar espacios extra
            String termino = username.trim();
            System.out.println("Término de búsqueda limpio: [" + termino + "]");
            
            List<Usuario> usuarios = usuarioService.buscarPorUsernameParcial(termino);
            System.out.println("Resultado final - Usuarios encontrados: " + usuarios.size());
            
            if (usuarios.isEmpty()) {
                System.out.println("No se encontraron usuarios para mostrar");
                // Retornar una lista vacía directamente
                return ResponseEntity.ok(usuarios);
            }
            
            // Imprimir los primeros 5 usuarios encontrados para debug
            for (int i = 0; i < Math.min(usuarios.size(), 5); i++) {
                Usuario u = usuarios.get(i);
                System.out.println("  - Usuario: " + u.getUsername() + ", ID: " + u.getId() + 
                                   ", Nombre: " + u.getNombreUsuario() + " " + u.getApellidoUsuario());
            }
            System.out.println("-------------------------------------------------------");
            
            return ResponseEntity.ok(usuarios);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error de validación: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            System.out.println("Error interno: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar usuarios: " + ex.getMessage()));
        }
    }
}
