package indie.config;

import indie.models.moduloNotificaciones.TipoNotificacion;
import indie.models.moduloNotificaciones.NotificationTypes;
import indie.repositories.moduloNotificaciones.TipoNotificacionRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class NotificationTypeInitializer implements ApplicationRunner {

    private final TipoNotificacionRepository tipoNotificacionRepository;

    public NotificationTypeInitializer(TipoNotificacionRepository tipoNotificacionRepository) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Canonical types that should always exist
        List<String> required = Arrays.asList(
                NotificationTypes.NUEVO_SEGUIDOR,
                NotificationTypes.INTERESADO_EVENTO,
                NotificationTypes.INVITACION_COLABORAR,
                NotificationTypes.NUEVO_COMENTARIO,
                NotificationTypes.MENSAJE_NUEVO
        );

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.YEAR, 10); // 10 years validity window
        Date baja = cal.getTime();

        for (String name : required) {
            tipoNotificacionRepository.findByNombreTipoNotificacionIgnoreCase(name)
                    .orElseGet(() -> {
                        TipoNotificacion t = TipoNotificacion.builder()
                                .nombreTipoNotificacion(name)
                                .notificarEmailTipoNotificacion(false)
                                .fechaAltaTipoNotificacion(now)
                                .fechaBajaTipoNotificacion(baja)
                                .build();
                        return tipoNotificacionRepository.save(t);
                    });
        }
    }
}
