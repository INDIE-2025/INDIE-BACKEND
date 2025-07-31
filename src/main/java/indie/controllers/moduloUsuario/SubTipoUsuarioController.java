package indie.controllers.moduloUsuario;


import indie.controllers.BaseController;
import indie.models.moduloUsuario.SubTipoUsuario;
import indie.services.moduloUsuario.SubTipoUsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subTipoUsuario")
public class SubTipoUsuarioController extends BaseController<SubTipoUsuario, String> {

    public SubTipoUsuarioController(SubTipoUsuarioService subTipoUsuarioService) {
        super(subTipoUsuarioService);
    }
}