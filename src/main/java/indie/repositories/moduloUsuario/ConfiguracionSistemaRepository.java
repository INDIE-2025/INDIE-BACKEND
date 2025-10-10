package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.ConfiguracionSistema;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, String> {

    Optional<ConfiguracionSistema> findByNombreConfiguracionIgnoreCase(String nombreConfiguracion);

    List<ConfiguracionSistema> findAllByDeletedAtIsNullOrderByNombreConfiguracionAsc();
}
