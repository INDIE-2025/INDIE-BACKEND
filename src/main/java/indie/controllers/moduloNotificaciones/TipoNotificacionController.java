package indie.controllers.moduloNotificaciones;

import indie.controllers.BaseController;
import indie.models.moduloNotificaciones.TipoNotificacion;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import indie.services.moduloNotificaciones.TipoNotificacionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipo-notificacion")
public class TipoNotificacionController extends BaseController<TipoNotificacion, String> {

    @Autowired
    protected TipoNotificacionServiceImpl tipoNotificacionService;

    public TipoNotificacionController(TipoNotificacionServiceImpl tipoNotificacionService) {
        super(tipoNotificacionService);
    }
}
