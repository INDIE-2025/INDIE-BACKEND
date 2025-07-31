package indie.services.moduloUsuario;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SubTipoUsuarioService extends BaseServiceImpl<SubTipoUsuario, String> {

    SubTipoUsuarioRepository subTipoUsuarioRepository;

    public SubTipoUsuarioService(SubTipoUsuarioRepository subTipoUsuarioRepository) {
        super(subTipoUsuarioRepository);
        this.subTipoUsuarioRepository = subTipoUsuarioRepository;
    }
}
