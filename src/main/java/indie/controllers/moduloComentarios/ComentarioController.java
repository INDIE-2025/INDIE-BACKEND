package indie.controllers.moduloComentarios;

import indie.controllers.BaseController;
import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.moduloComentarios.ComentarioServiceImpl;
import indie.services.moduloUsuario.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comentario")
public class ComentarioController extends BaseController<ComentarUsuario,String> {

    private final ComentarioServiceImpl comentarioService;
    private final UsuarioService usuarioService;

    public ComentarioController(ComentarioServiceImpl comentarioService, UsuarioService usuarioService){
        super(comentarioService);
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/traercomentarios")
    public ResponseEntity<?> traerComentariosDeUnUsuario(@RequestParam String idUsuarioComentado) {
        try {
            return ResponseEntity.ok(comentarioService.traerComentariosDeUnUsuario(idUsuarioComentado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    
    @PostMapping("/realizarComentario")
    public ResponseEntity<?> realizarComentario(
            @RequestParam String comentario,
            @RequestParam String idUsuarioComentado,
            @AuthenticationPrincipal String email
    ) {
        try {
            String idUsuarioComentador = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email))
                    .getId();

            ComentarioDTO dto = comentarioService.realizarComentario(
                    comentario, idUsuarioComentador, idUsuarioComentado
            );
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Error al realizar el comentario: " + e.getMessage() + "\"}");
        }
    }

   
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarComentario(
            @RequestParam String idComentario,
            @AuthenticationPrincipal String email
    ) {
        try {
            String idUsuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email))
                    .getId();

            comentarioService.eliminarComentario(idComentario, idUsuario);
            return ResponseEntity.ok("{\"message\": \"Comentario eliminado con éxito\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

  
    @PostMapping("/denunciar")
    public ResponseEntity<?> denunciarComentario(
            @RequestParam String idComentario,
            @RequestParam String motivoDenuncia,
            @AuthenticationPrincipal String email
    ) {
        try {
            String idUsuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email))
                    .getId();

            comentarioService.denunciarComentario(idComentario, idUsuario, motivoDenuncia);
            return ResponseEntity.ok("{\"message\": \"Comentario denunciado con éxito\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}

