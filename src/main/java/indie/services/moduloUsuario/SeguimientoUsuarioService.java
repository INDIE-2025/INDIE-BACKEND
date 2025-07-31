package indie.services.moduloUsuario;

import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.repositories.moduloUsuario.FuncionalidadRepository;
import indie.repositories.moduloUsuario.SeguimientoUsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SeguimientoUsuarioService extends BaseServiceImpl<SeguimientoUsuario, String> {

    SeguimientoUsuarioRepository seguimientoUsuarioRepository;

    public SeguimientoUsuarioService(SeguimientoUsuarioRepository seguimientoUsuarioRepository) {
        super(seguimientoUsuarioRepository);
        this.seguimientoUsuarioRepository = seguimientoUsuarioRepository;
    }

}
