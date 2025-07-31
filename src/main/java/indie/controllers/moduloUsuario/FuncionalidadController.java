package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.models.moduloUsuario.Funcionalidad;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.FuncionalidadService;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/funcionalidad")
public class FuncionalidadController extends BaseController<Funcionalidad, String> {

    public FuncionalidadController(FuncionalidadService funcionalidadService) {
        super(funcionalidadService);
    }
}

