package indie.services.moduloUsuario;

import indie.dtos.moduloUsuario.EstadisticasSeguimientoDTO;
import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloUsuario.SeguimientoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeguimientoUsuarioService extends BaseServiceImpl<SeguimientoUsuario, String> {

    private final SeguimientoUsuarioRepository seguimientoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    public SeguimientoUsuarioService(SeguimientoUsuarioRepository seguimientoUsuarioRepository, 
                                   UsuarioRepository usuarioRepository) {
        super(seguimientoUsuarioRepository);
        this.seguimientoUsuarioRepository = seguimientoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Seguir a un usuario
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

        // Buscar si existe una relación previa (incluyendo eliminadas lógicamente)
        SeguimientoUsuario relacionExistente = seguimientoUsuarioRepository.findBySeguidorAndSeguidoIncludingDeleted(seguidorId, seguidoId)
                .orElse(null);

        if (relacionExistente != null) {
            // Si la relación existe y no está eliminada lógicamente
            if (relacionExistente.getDeletedAt() == null && !relacionExistente.isBloqueado()) {
                throw new RuntimeException("Ya sigues a este usuario");
            }
            
            // Si está bloqueado
            if (relacionExistente.isBloqueado() && relacionExistente.getDeletedAt() == null) {
                throw new RuntimeException("No puedes seguir a un usuario que has bloqueado");
            }
            
            // Si fue eliminada lógicamente o bloqueada, reactivar la relación
            relacionExistente.setBloqueado(false);
            relacionExistente.setDeletedAt(null);
            relacionExistente.setUpdatedAt(LocalDateTime.now());
            return seguimientoUsuarioRepository.save(relacionExistente);
        }

        // Crear nueva relación si no existe ninguna previa
        SeguimientoUsuario seguimiento = SeguimientoUsuario.builder()
                .usuarioSeguidor(seguidor)
                .usuarioSeguido(seguido)
                .bloqueado(false)
                .build();

        return seguimientoUsuarioRepository.save(seguimiento);
    }

    /**
     * Dejar de seguir a un usuario (baja lógica)
     */
    @Transactional
    public void dejarDeSeguirUsuario(String seguidorId, String seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Un usuario no puede dejar de seguirse a sí mismo");
        }

        SeguimientoUsuario seguimiento = seguimientoUsuarioRepository.findBySeguidorAndSeguido(seguidorId, seguidoId)
                .orElseThrow(() -> new RuntimeException("No sigues a este usuario"));

        // Verificar que la relación no esté ya eliminada (baja lógica)
        if (seguimiento.getDeletedAt() != null) {
            throw new RuntimeException("No sigues a este usuario");
        }

        // Baja lógica: establecer deletedAt en lugar de eliminar físicamente
        seguimiento.setDeletedAt(LocalDateTime.now());
        seguimientoUsuarioRepository.save(seguimiento);
    }

    /**
     * Bloquear a un usuario
     */
    @Transactional
    public SeguimientoUsuario bloquearUsuario(String seguidorId, String usuarioABloquearId) {
        if (seguidorId.equals(usuarioABloquearId)) {
            throw new IllegalArgumentException("Un usuario no puede bloquearse a sí mismo");
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Usuario bloqueador no encontrado"));
        Usuario usuarioABloquear = usuarioRepository.findById(usuarioABloquearId)
                .orElseThrow(() -> new RuntimeException("Usuario a bloquear no encontrado"));

        // Verificar si ya está bloqueado
        if (seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, usuarioABloquearId)) {
            throw new RuntimeException("Este usuario ya está bloqueado");
        }

        // Buscar si existe una relación previa (incluyendo eliminadas lógicamente)
        SeguimientoUsuario relacionExistente = seguimientoUsuarioRepository.findBySeguidorAndSeguidoIncludingDeleted(seguidorId, usuarioABloquearId)
                .orElse(null);

        if (relacionExistente != null) {
            // Si existe una relación activa de seguimiento, la convertimos a bloqueo
            relacionExistente.setBloqueado(true);
            relacionExistente.setDeletedAt(null); // Asegurar que esté activa
            relacionExistente.setUpdatedAt(LocalDateTime.now());
            return seguimientoUsuarioRepository.save(relacionExistente);
        }

        // Crear nueva relación de bloqueo si no existe ninguna previa
        SeguimientoUsuario bloqueo = SeguimientoUsuario.builder()
                .usuarioSeguidor(seguidor)
                .usuarioSeguido(usuarioABloquear)
                .bloqueado(true)
                .build();

        return seguimientoUsuarioRepository.save(bloqueo);
    }

    /**
     * Desbloquear a un usuario
     */
    @Transactional
    public void desbloquearUsuario(String seguidorId, String usuarioADesbloqueaId) {
        if (seguidorId.equals(usuarioADesbloqueaId)) {
            throw new IllegalArgumentException("Un usuario no puede desbloquearse a sí mismo");
        }

        if (!seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, usuarioADesbloqueaId)) {
            throw new RuntimeException("Este usuario no está bloqueado");
        }

        // Buscar la relación de bloqueo activa
        SeguimientoUsuario seguimiento = seguimientoUsuarioRepository.findBySeguidorAndSeguido(seguidorId, usuarioADesbloqueaId)
                .orElseThrow(() -> new RuntimeException("Relación de bloqueo no encontrada"));
        
        // Realizar eliminación lógica de la relación de bloqueo
        seguimiento.setDeletedAt(LocalDateTime.now());
        seguimiento.setUpdatedAt(LocalDateTime.now());
        seguimientoUsuarioRepository.save(seguimiento);
    }

    /**
     * Traer seguidores de un usuario
     */
    public List<Usuario> traerSeguidores(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return seguimientoUsuarioRepository.findSeguidoresByUsuarioId(usuarioId);
    }

    /**
     * Traer seguidos de un usuario
     */
    public List<Usuario> traerSeguidos(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return seguimientoUsuarioRepository.findSeguidosByUsuarioId(usuarioId);
    }

    /**
     * Traer usuarios bloqueados por un usuario
     */
    public List<Usuario> traerUsuariosBloqueados(String usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return seguimientoUsuarioRepository.findUsuariosBloqueadosByUsuarioId(usuarioId);
    }

    /**
     * Contar seguidores de un usuario
     */
    public long contarSeguidores(String usuarioId) {
        return seguimientoUsuarioRepository.countSeguidoresByUsuarioId(usuarioId);
    }

    /**
     * Contar seguidos de un usuario
     */
    public long contarSeguidos(String usuarioId) {
        return seguimientoUsuarioRepository.countSeguidosByUsuarioId(usuarioId);
    }

    /**
     * Verificar si un usuario sigue a otro
     */
    public boolean verificarSiSigue(String seguidorId, String seguidoId) {
        return seguimientoUsuarioRepository.existsBySeguidorAndSeguido(seguidorId, seguidoId);
    }

    /**
     * Verificar si un usuario está bloqueado por otro
     */
    public boolean verificarSiEstaBloqueado(String seguidorId, String seguidoId) {
        return seguimientoUsuarioRepository.isUsuarioBloqueado(seguidorId, seguidoId);
    }

    /**
     * Obtener estadísticas de seguimiento de un usuario
     */
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
