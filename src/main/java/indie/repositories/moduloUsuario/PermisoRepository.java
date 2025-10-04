package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Permiso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, String> {

    Optional<Permiso> findByNombrePermisoIgnoreCase(String nombrepermiso);

    List<Permiso> findAllByOrderByNombrePermisoAsc();
}
