package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByEmailUsuario(String emailUsuario);

    boolean existsByEmailUsuario(String emailUsuario);


    List<Usuario> findAllByDeletedAtIsNull();

    long countBySubTipoUsuario_TipoUsuario_IdAndDeletedAtIsNull(String tipoUsuarioId);

    Optional<Usuario> findByEmailUsuarioIgnoreCase(String emailUsuario);

    boolean existsByEmailUsuarioIgnoreCase(String emailUsuario);

    Usuario findByUsername(String username);

    // Métodos para búsqueda
    List<Usuario> findByUsernameContainingIgnoreCaseOrNombreUsuarioContainingIgnoreCaseOrApellidoUsuarioContainingIgnoreCase(
            String username, String nombreUsuario, String apellidoUsuario, Pageable pageable);

}
