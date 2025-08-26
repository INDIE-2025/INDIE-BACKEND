package indie.controllers.moduloComentarios;

import indie.controllers.BaseController;
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

    @GetMapping("/comentarios")
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
            comentarioService.realizarComentario(comentario, idUsuarioComentador, idUsuarioComentado);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Comentario creado exitosamente\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
