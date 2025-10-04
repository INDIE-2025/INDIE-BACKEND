package indie.bootstrap;

import indie.models.moduloUsuario.ConfiguracionSistema;
import indie.models.moduloUsuario.Permiso;
import indie.models.moduloUsuario.PermisoTipoUsuario;
import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloUsuario.ConfiguracionSistemaRepository;
import indie.repositories.moduloUsuario.PermisoRepository;
import indie.repositories.moduloUsuario.PermisoTipoUsuarioRepository;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupDataLoader implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@indie.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin123!";

    private static final List<ConfiguracionSeed> CONFIGURACIONES = List.of(
        new ConfiguracionSeed("Eventos - descripcion", true, null),
        new ConfiguracionSeed("Eventos - direccion", true, null),
        new ConfiguracionSeed("Eventos - mapa", true, null),
        new ConfiguracionSeed("Eventos - cantidad de imagenes", true, 5),
        new ConfiguracionSeed("Notificaciones - Actividad del sistema - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Actividad del sistema - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Proximidad de eventos - genera", false, null),
        new ConfiguracionSeed("Notificaciones - Proximidad de eventos - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Invitacion a colaborar - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Invitacion a colaborar - notificarPorMail", true, null),
        new ConfiguracionSeed("Notificaciones - Colaboracion aceptada - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Colaboracion aceptada - notificarPorMail", true, null),
        new ConfiguracionSeed("Notificaciones - Colaboracion rechazada - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Colaboracion rechazada - notificarPorMail", true, null),
        new ConfiguracionSeed("Notificaciones - Comentarios - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Comentarios - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Seguimiento - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Seguimiento - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Interes en evento propio - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Interes en evento propio - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Comentario denunciado eliminado - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Comentario denunciado eliminado - notificarPorMail", false, null),
        new ConfiguracionSeed("Notificaciones - Denuncia rechazada - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Denuncia rechazada - notificarPorMail", true, null),
        new ConfiguracionSeed("Notificaciones - Mensaje nuevo - genera", true, null),
        new ConfiguracionSeed("Notificaciones - Mensaje nuevo - notificarPorMail", false, null),
        new ConfiguracionSeed("Recomendaciones - Filtro - Subtipo de usuario", true, null),
        new ConfiguracionSeed("Recomendaciones - Orden - Eventos cercanos", false, null),
        new ConfiguracionSeed("Recomendaciones - Orden - Seguidores o interesados", true, null),
        new ConfiguracionSeed("Reportes - Reportes de usuario - Visualizaciones del perfil", true, null),
        new ConfiguracionSeed("Reportes - Reportes de usuario - Usuarios interesados", true, null),
        new ConfiguracionSeed("Reportes - Reportes de usuario - Eventos mas populares", true, null),
        new ConfiguracionSeed("Reportes - Reportes de usuario - Cantidad de seguidores", true, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Nuevos usuarios en el sistema", true, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Comentarios realizados en el sistema", true, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Eventos publicados en el sistema", false, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Inicios de sesion unicos en el sistema", true, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Usuarios populares", true, null),
        new ConfiguracionSeed("Reportes - Reportes de administrador - Eventos populares", true, null)
    );

    private static final Map<String, List<PermisoSeed>> PERMISOS = Map.of(
        "Administrador", List.of(
            new PermisoSeed("Visualizar Reportes y estadisticas", true),
            new PermisoSeed("Gestionar eventos", true),
            new PermisoSeed("Seccion de comentarios", true),
            new PermisoSeed("Recibir colaboraciones", true),
            new PermisoSeed("Mensajeria", true),
            new PermisoSeed("Perfil publico", true),
            new PermisoSeed("Geolocalizacion", true),
            new PermisoSeed("Calendario", true),
            new PermisoSeed("Contenido digital", true)
        ),
        "Establecimiento", List.of(
            new PermisoSeed("Visualizar Reportes y estadisticas", true),
            new PermisoSeed("Gestionar eventos", true),
            new PermisoSeed("Seccion de comentarios", true),
            new PermisoSeed("Recibir colaboraciones", true),
            new PermisoSeed("Mensajeria", true),
            new PermisoSeed("Perfil publico", true),
            new PermisoSeed("Geolocalizacion", true),
            new PermisoSeed("Calendario", true),
            new PermisoSeed("Contenido digital", true)
        ),
        "Fan", List.of(
            new PermisoSeed("Visualizar Reportes y estadisticas", true),
            new PermisoSeed("Gestionar eventos", false),
            new PermisoSeed("Seccion de comentarios", true),
            new PermisoSeed("Recibir colaboraciones", false),
            new PermisoSeed("Mensajeria", false),
            new PermisoSeed("Perfil publico", false),
            new PermisoSeed("Geolocalizacion", false),
            new PermisoSeed("Calendario", false),
            new PermisoSeed("Contenido digital", false)
        ),
        "Artista", List.of(
            new PermisoSeed("Visualizar Reportes y estadisticas", true),
            new PermisoSeed("Gestionar eventos", true),
            new PermisoSeed("Seccion de comentarios", true),
            new PermisoSeed("Recibir colaboraciones", true),
            new PermisoSeed("Mensajeria", true),
            new PermisoSeed("Perfil publico", true),
            new PermisoSeed("Geolocalizacion", false),
            new PermisoSeed("Calendario", true),
            new PermisoSeed("Contenido digital", true)
        )
    );

    private final ConfiguracionSistemaRepository configuracionSistemaRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SubTipoUsuarioRepository subTipoUsuarioRepository;
    private final PermisoRepository permisoRepository;
    private final PermisoTipoUsuarioRepository permisoTipoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedConfiguraciones();
        Map<String, Permiso> permisos = seedPermisos();
        Map<String, TipoUsuario> tiposUsuario = seedTiposUsuario();
        Map<String, SubTipoUsuario> subtipos = seedSubtiposBase(tiposUsuario);
        seedPermisosPorTipo(tiposUsuario, permisos);
        seedAdministrador(subtipos.get("Administrador"));
    }

    private Map<String, ConfiguracionSistema> seedConfiguraciones() {
        Map<String, ConfiguracionSistema> resultado = new HashMap<>();
        for (ConfiguracionSeed seed : CONFIGURACIONES) {
            ConfiguracionSistema configuracion = configuracionSistemaRepository
                .findByNombreConfiguracionIgnoreCase(seed.nombre())
                .orElseGet(() -> ConfiguracionSistema.builder()
                    .nombreConfiguracion(seed.nombre())
                    .build());
            configuracion.setActivo(seed.activo());
            if (seed.valor() != null) {
                configuracion.setValor(seed.valor());
            }
            configuracionSistemaRepository.save(configuracion);
            resultado.put(seed.nombre(), configuracion);
        }
        return resultado;
    }

    private Map<String, Permiso> seedPermisos() {
        Map<String, Permiso> resultado = new HashMap<>();
        PERMISOS.values().forEach(lista -> lista.forEach(seed -> {
            Permiso permiso = permisoRepository.findByNombrePermisoIgnoreCase(seed.nombre())
                .orElseGet(() -> {
                    Permiso nuevo = new Permiso();
                    nuevo.setNombrePermiso(seed.nombre());
                    return nuevo;
                });
            permisoRepository.save(permiso);
            resultado.put(seed.nombre(), permiso);
        }));
        return resultado;
    }

    private Map<String, TipoUsuario> seedTiposUsuario() {
        Map<String, TipoUsuario> resultado = new HashMap<>();
        for (String nombre : PERMISOS.keySet()) {
            TipoUsuario tipo = tipoUsuarioRepository.findByNombreTipoUsuarioIgnoreCase(nombre)
                .orElseGet(() -> {
                    TipoUsuario nuevo = new TipoUsuario();
                    nuevo.setNombreTipoUsuario(nombre);
                    return nuevo;
                });
            tipo.setDeletedAt(null);
            tipoUsuarioRepository.save(tipo);
            resultado.put(nombre, tipo);
        }
        return resultado;
    }

    private Map<String, SubTipoUsuario> seedSubtiposBase(Map<String, TipoUsuario> tipos) {
        Map<String, SubTipoUsuario> resultado = new HashMap<>();
        for (Map.Entry<String, TipoUsuario> entry : tipos.entrySet()) {
            String nombreTipo = entry.getKey();
            TipoUsuario tipo = entry.getValue();
            String nombreSubtipo = nombreTipo;
            SubTipoUsuario subTipo = subTipoUsuarioRepository
                .findByTipoUsuario_IdAndNombreSubTipoUsuarioIgnoreCase(tipo.getId(), nombreSubtipo)
                .orElseGet(() -> {
                    SubTipoUsuario nuevo = new SubTipoUsuario();
                    nuevo.setNombreSubTipoUsuario(nombreSubtipo);
                    nuevo.setTipoUsuario(tipo);
                    return nuevo;
                });
            subTipo.setDeletedAt(null);
            subTipoUsuarioRepository.save(subTipo);
            resultado.put(nombreTipo, subTipo);
        }
        return resultado;
    }

    private void seedPermisosPorTipo(Map<String, TipoUsuario> tipos, Map<String, Permiso> permisos) {
        for (Map.Entry<String, List<PermisoSeed>> entry : PERMISOS.entrySet()) {
            TipoUsuario tipoUsuario = tipos.get(entry.getKey());
            if (tipoUsuario == null) {
                continue;
            }
            for (PermisoSeed permisoSeed : entry.getValue()) {
                Permiso permiso = permisos.get(permisoSeed.nombre());
                if (permiso == null) {
                    continue;
                }
                PermisoTipoUsuario permisoTipoUsuario = permisoTipoUsuarioRepository
                    .findByTipoUsuarioAndPermiso(tipoUsuario, permiso)
                    .orElseGet(() -> {
                        PermisoTipoUsuario nuevo = new PermisoTipoUsuario();
                        nuevo.setTipoUsuario(tipoUsuario);
                        nuevo.setPermiso(permiso);
                        return nuevo;
                    });
                permisoTipoUsuario.setActivo(permisoSeed.activo());
                permisoTipoUsuarioRepository.save(permisoTipoUsuario);
            }
        }
    }

    private void seedAdministrador(SubTipoUsuario subTipoAdministrador) {
        if (subTipoAdministrador == null) {
            return;
        }
        boolean existeAdmin = usuarioRepository.existsByEmailUsuarioIgnoreCase(ADMIN_EMAIL)
            || usuarioRepository.findByUsername(ADMIN_USERNAME) != null;
        if (existeAdmin) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombreUsuario("Administrador del sistema");
        admin.setEmailUsuario(ADMIN_EMAIL);
        admin.setUsername(ADMIN_USERNAME);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setFechaVerificacion(LocalDateTime.now());
        admin.setSubTipoUsuario(subTipoAdministrador);
        usuarioRepository.save(admin);
    }

    private record ConfiguracionSeed(String nombre, boolean activo, Integer valor) {
    }

    private record PermisoSeed(String nombre, boolean activo) {
    }
}
