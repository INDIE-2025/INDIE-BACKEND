package indie.repositories.moduloNotificaciones;

import indie.models.moduloNotificaciones.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, String> {
}
