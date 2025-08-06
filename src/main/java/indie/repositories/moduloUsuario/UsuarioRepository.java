package indie.repositories.moduloUsuario;

import org.springframework.data.jpa.repository.JpaRepository;

import indie.models.moduloUsuario.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByEmailUsuario(String emailUsuario);

    boolean existsByEmailUsuario(String emailUsuario);
}
