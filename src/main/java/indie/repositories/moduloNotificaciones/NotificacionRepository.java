package indie.repositories.moduloNotificaciones;

import indie.models.moduloNotificaciones.Notificacion;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, String> {
    List<Notificacion> findByIdUsuarioAndFechaLecturaNotificacionIsNullOrderByCreatedAtAsc(Usuario usuario);
    List<Notificacion> findByIdInAndIdUsuario(Collection<String> ids, Usuario usuario);
    List<Notificacion> findTop6ByIdUsuarioOrderByCreatedAtDesc(Usuario usuario);
}
