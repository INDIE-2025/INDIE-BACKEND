package indie.repositories.moduloNotificaciones;

import indie.models.moduloNotificaciones.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, String> {
    Optional<TipoNotificacion> findByNombreTipoNotificacionIgnoreCase(String nombreTipoNotificacion);
}
