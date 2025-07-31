package indie.services.moduloUsuario;

import indie.models.moduloUsuario.TipoUsuario;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TipoUsuarioService extends BaseServiceImpl<TipoUsuario, String> {

    TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        super(tipoUsuarioRepository);
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

}
