package indie.repositories.moduloNotificaciones;

import indie.models.moduloNotificaciones.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, String> {
}
