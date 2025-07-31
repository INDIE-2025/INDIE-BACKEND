package indie.services.moduloNotificaciones;

import indie.models.moduloNotificaciones.TipoNotificacionUsuario;
import indie.repositories.moduloNotificaciones.TipoNotificacionUsuarioRepository;
import indie.services.BaseServiceImpl;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoNotificacionUsuarioServiceImpl extends BaseServiceImpl<TipoNotificacionUsuario, String> implements TipoNotificacionUsuarioService {

    TipoNotificacionUsuarioRepository tipoNotificacionUsuarioRepository;

    @Autowired
    public TipoNotificacionUsuarioServiceImpl(TipoNotificacionUsuarioRepository tipoNotificacionUsuarioRepository) {
        super(tipoNotificacionUsuarioRepository);
        this.tipoNotificacionUsuarioRepository = tipoNotificacionUsuarioRepository;
    }
}
