package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.SeguimientoUsuario;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguimientoUsuarioRepository extends JpaRepository<SeguimientoUsuario, String> {
    
    // Buscar relación específica entre dos usuarios (solo activas)
    @Query("SELECT s FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :seguidorId AND s.usuarioSeguido.id = :seguidoId AND s.deletedAt IS NULL")
    Optional<SeguimientoUsuario> findBySeguidorAndSeguido(@Param("seguidorId") String seguidorId, @Param("seguidoId") String seguidoId);
    
    // Buscar relación específica entre dos usuarios (incluyendo eliminadas lógicamente)
    @Query("SELECT s FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :seguidorId AND s.usuarioSeguido.id = :seguidoId")
    Optional<SeguimientoUsuario> findBySeguidorAndSeguidoIncludingDeleted(@Param("seguidorId") String seguidorId, @Param("seguidoId") String seguidoId);
    
    // Verificar si un usuario sigue a otro (solo relaciones activas)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :seguidorId AND s.usuarioSeguido.id = :seguidoId AND s.bloqueado = false AND s.deletedAt IS NULL")
    boolean existsBySeguidorAndSeguido(@Param("seguidorId") String seguidorId, @Param("seguidoId") String seguidoId);
    
    // Verificar si un usuario está bloqueado por otro (solo relaciones activas)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :seguidorId AND s.usuarioSeguido.id = :seguidoId AND s.bloqueado = true AND s.deletedAt IS NULL")
    boolean isUsuarioBloqueado(@Param("seguidorId") String seguidorId, @Param("seguidoId") String seguidoId);
    
    // Obtener lista de seguidores de un usuario (no bloqueados y activos)
    @Query("SELECT s.usuarioSeguidor FROM SeguimientoUsuario s WHERE s.usuarioSeguido.id = :usuarioId AND s.bloqueado = false AND s.deletedAt IS NULL")
    List<Usuario> findSeguidoresByUsuarioId(@Param("usuarioId") String usuarioId);
    
    // Obtener lista de usuarios seguidos por un usuario (no bloqueados y activos)
    @Query("SELECT s.usuarioSeguido FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :usuarioId AND s.bloqueado = false AND s.deletedAt IS NULL")
    List<Usuario> findSeguidosByUsuarioId(@Param("usuarioId") String usuarioId);
    
    // Contar seguidores de un usuario (solo activos)
    @Query("SELECT COUNT(s) FROM SeguimientoUsuario s WHERE s.usuarioSeguido.id = :usuarioId AND s.bloqueado = false AND s.deletedAt IS NULL")
    long countSeguidoresByUsuarioId(@Param("usuarioId") String usuarioId);
    
    // Contar usuarios seguidos por un usuario (solo activos)
    @Query("SELECT COUNT(s) FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :usuarioId AND s.bloqueado = false AND s.deletedAt IS NULL")
    long countSeguidosByUsuarioId(@Param("usuarioId") String usuarioId);
    
    // Obtener usuarios bloqueados por un usuario (solo activos)
    @Query("SELECT s.usuarioSeguido FROM SeguimientoUsuario s WHERE s.usuarioSeguidor.id = :usuarioId AND s.bloqueado = true AND s.deletedAt IS NULL")
    List<Usuario> findUsuariosBloqueadosByUsuarioId(@Param("usuarioId") String usuarioId);
    
}

