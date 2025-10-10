package indie.services.moduloUsuario;

import indie.models.moduloUsuario.UsuarioUbicacion;
import indie.repositories.moduloUsuario.UsuarioUbicacionRepository;
import indie.services.BaseServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioUbicacionService extends BaseServiceImpl<UsuarioUbicacion, String> {

    private final UsuarioUbicacionRepository repository;

    public UsuarioUbicacionService(UsuarioUbicacionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<UsuarioUbicacion> findByUsuarioId(String usuarioId) {
        return repository.findByUsuario_Id(usuarioId);
    }
}

