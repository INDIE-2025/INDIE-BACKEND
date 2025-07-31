package indie.controllers.moduloNotificaciones;

import indie.controllers.BaseController;
import indie.models.moduloNotificaciones.TipoNotificacionUsuario;
import indie.services.moduloNotificaciones.TipoNotificacionUsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipo-notificacion-usuario")
public class TipoNotificacionUsuarioController extends BaseController<TipoNotificacionUsuario, String> {

    @Autowired
    protected TipoNotificacionUsuarioServiceImpl tipoNotificacionUsuarioService;

    public TipoNotificacionUsuarioController(TipoNotificacionUsuarioServiceImpl tipoNotificacionUsuarioService) {
        super(tipoNotificacionUsuarioService);
    }


}

