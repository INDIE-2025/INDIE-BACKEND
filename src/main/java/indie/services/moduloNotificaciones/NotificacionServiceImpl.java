package indie.services.moduloNotificaciones;

import indie.models.moduloNotificaciones.Notificacion;
import indie.models.moduloNotificaciones.TipoNotificacion;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloNotificaciones.NotificacionRepository;
import indie.repositories.moduloNotificaciones.TipoNotificacionRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificacionServiceImpl extends BaseServiceImpl<Notificacion, String> implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   TipoNotificacionRepository tipoNotificacionRepository) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    public Notificacion crear(Usuario usuario, String tipoNombre, String contenido) {
        Date ahora = new Date();
        // 10 años de vigencia como valor simple
        Date baja = new Date(ahora.getTime() + 10L * 365 * 24 * 60 * 60 * 1000);

        TipoNotificacion tipo = tipoNotificacionRepository
                .findByNombreTipoNotificacionIgnoreCase(tipoNombre)
                .orElseGet(() -> {
                    TipoNotificacion nuevo = TipoNotificacion.builder()
                            .nombreTipoNotificacion(tipoNombre)
                            .notificarEmailTipoNotificacion(false)
                            .fechaAltaTipoNotificacion(ahora)
                            .fechaBajaTipoNotificacion(baja)
                            .build();
                    return tipoNotificacionRepository.save(nuevo);
                });

        Notificacion n = Notificacion.builder()
                .idUsuario(usuario)
                .idTipoNotificacion(tipo)
                .contenidoNotificacion(contenido)
                .fechaAltaNotificacion(ahora)
                .fechaBajaNotificacion(baja)
                .build();
        return notificacionRepository.save(n);
    }

    public List<Notificacion> unreadFor(Usuario usuario) {
        return notificacionRepository.findByIdUsuarioAndFechaLecturaNotificacionIsNullOrderByCreatedAtAsc(usuario);
    }

    public List<Notificacion> recentFor(Usuario usuario, int limit) {
        // Repositorio expone top6; si el límite es 6 devolvemos ese; si no, hacemos una lectura general y cortamos
        if (limit == 6) {
            return notificacionRepository.findTop6ByIdUsuarioOrderByCreatedAtDesc(usuario);
        }
        List<Notificacion> all = notificacionRepository.findTop6ByIdUsuarioOrderByCreatedAtDesc(usuario);
        return all.size() <= limit ? all : all.subList(0, limit);
    }

    public void markRead(Usuario usuario, List<String> ids) {
        if (ids == null || ids.isEmpty()) return;
        List<Notificacion> list = notificacionRepository.findByIdInAndIdUsuario(ids, usuario);
        Date ahora = new Date();
        list.forEach(n -> n.setFechaLecturaNotificacion(ahora));
        notificacionRepository.saveAll(list);
    }
}
