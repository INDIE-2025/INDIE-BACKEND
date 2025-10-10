package indie.services.moduloUsuario;

import indie.dtos.moduloUsuario.EstadisticasSeguimientoDTO;
import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloUsuario.SeguimientoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeguimientoUsuarioService extends BaseServiceImpl<SeguimientoUsuario, String> {

    private final SeguimientoUsuarioRepository seguimientoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionServiceImpl notificacionService;

    public SeguimientoUsuarioService(SeguimientoUsuarioRepository seguimientoUsuarioRepository,
                                     UsuarioRepository usuarioRepository,
                                     NotificacionServiceImpl notificacionService) {
        super(seguimientoUsuarioRepository);
        this.seguimientoUsuarioRepository = seguimientoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Seguir a un usuario (reactiva si existía soft-deleted).
     */
    @Transactional
    public SeguimientoUsuario seguirUsuario(String seguidorId, String seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Un usuario no puede seguirse a sí mismo");
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Usuario seguidor no encontrado"));
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuario a seguir no encontrado"));

        // si está bloqueado activamente -> no permitir follow
        if (seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, seguidoId)) {
            throw new RuntimeException("No puedes seguir a un usuario que has bloqueado");
        }

        // ¿ya existe relación activa de follow?
        if (seguimientoUsuarioRepository.findBySeguidorAndSeguido(seguidorId, seguidoId)
                .filter(s -> !s.isBloqueado()) // activa y no bloqueada
                .isPresent()) {
            throw new RuntimeException("Ya sigues a este usuario");
        }

        // Buscar relación previa (incluye soft-deleted)
        SeguimientoUsuario relPrev =
                seguimientoUsuarioRepository.findBySeguidorAndSeguidoIncludingDeleted(seguidorId, seguidoId)
                        .orElse(null);

        if (relPrev != null) {
            // Reactivar como follow (activa, no bloqueada)
            relPrev.setBloqueado(false);
            relPrev.setDeletedAt(null);
            relPrev.setUpdatedAt(LocalDateTime.now());
            SeguimientoUsuario res = seguimientoUsuarioRepository.save(relPrev);
            // Notificar al seguido
            String contenido = seguidor.getNombreUsuario() + " te empezo a seguir";
            notificacionService.crear(seguido, "Nuevo seguidor", contenido);
            return res;
        }

        // Crear nueva
        SeguimientoUsuario nueva = SeguimientoUsuario.builder()
                .usuarioSeguidor(seguidor)
                .usuarioSeguido(seguido)
                .bloqueado(false)
                .build();

        SeguimientoUsuario res = seguimientoUsuarioRepository.save(nueva);
        String contenido = seguidor.getNombreUsuario() + " te empezo a seguir";
        notificacionService.crear(seguido, "Nuevo seguidor", contenido);
        return res;
    }

    /**
     * Dejar de seguir (soft delete).
     */
    @Transactional
    public void dejarDeSeguirUsuario(String seguidorId, String seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Un usuario no puede dejar de seguirse a sí mismo");
        }

        // Solo relaciones activas (findBySeguidorAndSeguido ya filtra deletedAt IS NULL)
        SeguimientoUsuario relActiva = seguimientoUsuarioRepository.findBySeguidorAndSeguido(seguidorId, seguidoId)
                .orElseThrow(() -> new RuntimeException("No sigues a este usuario"));

        // Si estaba bloqueada, igualmente se soft-deleted y deja de estar activa
        relActiva.setDeletedAt(LocalDateTime.now());
        relActiva.setUpdatedAt(LocalDateTime.now());
        seguimientoUsuarioRepository.save(relActiva);
    }

    /**
     * Bloquear a un usuario (convierte la relación a bloqueada activa).
     */
    @Transactional
    public SeguimientoUsuario bloquearUsuario(String seguidorId, String usuarioABloquearId) {
        if (seguidorId.equals(usuarioABloquearId)) {
            throw new IllegalArgumentException("Un usuario no puede bloquearse a sí mismo");
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Usuario bloqueador no encontrado"));
        Usuario bloqueado = usuarioRepository.findById(usuarioABloquearId)
                .orElseThrow(() -> new RuntimeException("Usuario a bloquear no encontrado"));

        // si ya hay bloqueo activo -> error
        if (seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, usuarioABloquearId)) {
            throw new RuntimeException("Este usuario ya está bloqueado");
        }

        SeguimientoUsuario rel = seguimientoUsuarioRepository
                .findBySeguidorAndSeguidoIncludingDeleted(seguidorId, usuarioABloquearId)
                .orElseGet(() -> SeguimientoUsuario.builder()
                        .usuarioSeguidor(seguidor)
                        .usuarioSeguido(bloqueado)
                        .build());

        rel.setBloqueado(true);
        rel.setDeletedAt(null); // activa (para que el bloqueo esté vigente)
        rel.setUpdatedAt(LocalDateTime.now());

        return seguimientoUsuarioRepository.save(rel);
    }

    /**
     * Desbloquear a un usuario (no reactiva follow: elimina lógicamente la relación).
     */
    @Transactional
    public void desbloquearUsuario(String seguidorId, String usuarioADesbloqueaId) {
        if (seguidorId.equals(usuarioADesbloqueaId)) {
            throw new IllegalArgumentException("Un usuario no puede desbloquearse a sí mismo");
        }

        if (!seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, usuarioADesbloqueaId)) {
            throw new RuntimeException("Este usuario no está bloqueado");
        }

        // Relación activa (bloqueada) -> soft delete
        SeguimientoUsuario rel = seguimientoUsuarioRepository
                .findBySeguidorAndSeguido(seguidorId, usuarioADesbloqueaId)
                .orElseThrow(() -> new RuntimeException("Relación de bloqueo no encontrada"));

        rel.setDeletedAt(LocalDateTime.now());
        rel.setUpdatedAt(LocalDateTime.now());
        seguimientoUsuarioRepository.save(rel);
    }

    /* ===================== LECTURAS (readOnly) ===================== */

    @Transactional(readOnly = true)
    public List<Usuario> traerSeguidores(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return seguimientoUsuarioRepository.findSeguidoresByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Usuario> traerSeguidos(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return seguimientoUsuarioRepository.findSeguidosByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Usuario> traerUsuariosBloqueados(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return seguimientoUsuarioRepository.findUsuariosBloqueadosByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public long contarSeguidores(String usuarioId) {
        return seguimientoUsuarioRepository.countSeguidoresByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public long contarSeguidos(String usuarioId) {
        return seguimientoUsuarioRepository.countSeguidosByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public boolean verificarSiSigue(String seguidorId, String seguidoId) {
        return seguimientoUsuarioRepository.existsBySeguidorAndSeguido(seguidorId, seguidoId);
    }

    @Transactional(readOnly = true)
    public boolean verificarSiEstaBloqueado(String seguidorId, String seguidoId) {
        return seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, seguidoId);
    }


    @Transactional(readOnly = true)
    public boolean verificarSiEstaSiendoBloqueado(String usuarioActualId, String otroUsuarioId) {
        return seguimientoUsuarioRepository.isUsuarioBloqueado(otroUsuarioId, usuarioActualId);
    }

    @Transactional(readOnly = true)
    public EstadisticasSeguimientoDTO obtenerEstadisticas(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return EstadisticasSeguimientoDTO.builder()
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidoUsuario(usuario.getApellidoUsuario())
                .username(usuario.getUsername())
                .totalSeguidores(contarSeguidores(usuarioId))
                .totalSeguidos(contarSeguidos(usuarioId))
                .build();
    }
}
