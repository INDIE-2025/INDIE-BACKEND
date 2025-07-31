package indie.repositories.moduloNotificaciones;

import indie.models.moduloNotificaciones.TipoNotificacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoNotificacionUsuarioRepository extends JpaRepository<TipoNotificacionUsuario, String> {
}
