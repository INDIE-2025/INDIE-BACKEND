package indie.controllers;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.ComentarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController extends BaseController<ComentarUsuario,String> {

    @Autowired
    protected ComentarioServiceImpl comentarioService;

    public ComentarioController(ComentarioServiceImpl comentarioService){
        super(comentarioService);
    }

    @GetMapping("/comentarios")
    public ResponseEntity<?> traerComentariosDeUnUsuario(String idUsuarioComentado) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(comentarioService.traerComentariosDeUnUsuario(idUsuarioComentado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"error\": \"" + e.getMessage() + "\"}"));
        }
    }
}
