package indie.services.moduloNotificaciones;

import indie.models.moduloNotificaciones.TipoNotificacion;
import indie.repositories.moduloNotificaciones.TipoNotificacionRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoNotificacionServiceImpl extends BaseServiceImpl<TipoNotificacion, String> implements TipoNotificacionService {

    TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    public TipoNotificacionServiceImpl(TipoNotificacionRepository tipoNotificacionRepository) {
        super(tipoNotificacionRepository);
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }
}
