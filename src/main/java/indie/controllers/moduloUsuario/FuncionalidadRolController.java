package indie.controllers.moduloUsuario;

import indie.controllers.BaseController;
import indie.models.moduloUsuario.FuncionalidadRol;
import indie.services.moduloUsuario.FuncionalidadRolService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/funcionalidadRol")
public class FuncionalidadRolController extends BaseController<FuncionalidadRol, String> {

    public FuncionalidadRolController(FuncionalidadRolService funcionalidadRolService) {
        super(funcionalidadRolService);
    }
}
