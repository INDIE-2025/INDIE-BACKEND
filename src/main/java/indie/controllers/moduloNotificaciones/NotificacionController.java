package indie.controllers.moduloNotificaciones;

import indie.controllers.BaseController;
import indie.models.moduloNotificaciones.Notificacion;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificacion")
public class NotificacionController extends BaseController<Notificacion, String> {

    @Autowired
    protected NotificacionServiceImpl notificacionService;

    public NotificacionController(NotificacionServiceImpl notificacionService) {
        super(notificacionService);
    }
}
