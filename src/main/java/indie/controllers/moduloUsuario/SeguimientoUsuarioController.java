package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.SeguimientoUsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seguimientoUsuario")
public class SeguimientoUsuarioController extends BaseController<SeguimientoUsuario, String> {

    public SeguimientoUsuarioController(SeguimientoUsuarioService seguimientoUsuarioService) {
        super(seguimientoUsuarioService);
    }
}
