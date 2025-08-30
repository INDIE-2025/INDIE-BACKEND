package indie.controllers.moduloComentarios;

import indie.controllers.BaseController;
import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.moduloComentarios.ComentarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController extends BaseController<ComentarUsuario,String> {

    @Autowired
    protected ComentarioServiceImpl comentarioService;

    public ComentarioController(ComentarioServiceImpl comentarioService){
        super(comentarioService);
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
    public ResponseEntity<?> realizarComentario(@RequestParam String comentario, @RequestParam String idUsuarioComentador, @RequestParam String idUsuarioComentado) {
        try {
            ComentarioDTO dto = comentarioService.realizarComentario(comentario, idUsuarioComentador, idUsuarioComentado);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Error al realizar el comentario: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarComentario(@RequestParam String idComentario, @RequestParam String idUsuario) {
        try {
            comentarioService.eliminarComentario(idComentario, idUsuario);
            return ResponseEntity.ok("{\"message\": \"Comentario eliminado con éxito\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/denunciar")
    public ResponseEntity<?> denunciarComentario(@RequestParam String idComentario, @RequestParam String idUsuario, @RequestParam String motivoDenuncia) {
        try {
            comentarioService.denunciarComentario(idComentario, idUsuario, motivoDenuncia);
            return ResponseEntity.ok("{\"message\": \"Comentario denunciado con éxito\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
