package indie.controllers;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.ComentarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController extends BaseController<ComentarUsuario,Long> {

    public ComentarioController(ComentarioService comentarioService){
        super(comentarioService);
    }
}
