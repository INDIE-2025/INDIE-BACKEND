package indie.services;

import org.springframework.data.jpa.repository.JpaRepository;

import indie.models.Usuario;

public class UsuarioService extends BaseServiceImpl<Usuario, Long> {

    public UsuarioService(JpaRepository<Usuario, Long> repository) {
        super(repository);
    }
    
}
