package indie.services;

import indie.repositories.UsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import indie.models.moduloUsuario.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, Long> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }
}
