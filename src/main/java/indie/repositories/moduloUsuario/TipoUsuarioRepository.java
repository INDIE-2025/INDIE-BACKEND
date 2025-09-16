package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.TipoUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, String> {

    List<TipoUsuario> findAllByDeletedAtIsNull();

    Optional<TipoUsuario> findByNombreTipoUsuarioIgnoreCase(String nombreTipoUsuario);

    boolean existsByNombreTipoUsuarioIgnoreCase(String nombreTipoUsuario);
}
