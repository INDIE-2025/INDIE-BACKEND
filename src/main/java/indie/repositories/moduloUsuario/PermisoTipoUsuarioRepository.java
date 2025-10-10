package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Permiso;
import indie.models.moduloUsuario.PermisoTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoTipoUsuarioRepository extends JpaRepository<PermisoTipoUsuario, String> {

    Optional<PermisoTipoUsuario> findByTipoUsuarioAndPermiso(TipoUsuario tipoUsuario, Permiso permiso);

    List<PermisoTipoUsuario> findByTipoUsuario(TipoUsuario tipoUsuario);
}
