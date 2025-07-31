package indie.services.moduloNotificaciones;

import indie.models.moduloNotificaciones.Notificacion;
import indie.repositories.moduloNotificaciones.NotificacionRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionServiceImpl extends BaseServiceImpl<Notificacion, String> implements NotificacionService {

    NotificacionRepository notificacionRepository;

    @Autowired
    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository;
    }
}
