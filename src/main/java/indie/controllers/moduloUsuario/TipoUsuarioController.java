package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.models.moduloUsuario.TipoUsuario;
import indie.services.moduloUsuario.TipoUsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipoUsuario")
public class TipoUsuarioController extends BaseController<TipoUsuario, String> {

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        super(tipoUsuarioService);
    }
}
