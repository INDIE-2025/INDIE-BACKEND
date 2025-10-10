package indie.repositories.moduloEventos;

import indie.models.moduloEventos.Evento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import indie.models.enums.eventoEstado;

public interface EventoRepository extends JpaRepository<Evento, String> {

    @Query("SELECT e FROM Evento e WHERE e.estadoEvento = :estado AND e.idUsuario.id = :idUsuario AND e.fechaBajaEvento IS NULL")
    List<Evento> findByEstadoEventoAndIdUsuario(@Param("estado") eventoEstado estado, @Param("idUsuario") String idUsuario);
    
    @Query("SELECT e FROM Evento e WHERE e.tituloEvento = :titulo AND e.estadoEvento = :estado AND e.idUsuario.id = :idUsuario AND e.fechaBajaEvento IS NULL")
    Optional<Evento> findByTituloAndEstadoAndIdUsuario(@Param("titulo") String titulo, @Param("estado") eventoEstado estado, @Param("idUsuario") String idUsuario);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evento e WHERE e.tituloEvento = :titulo AND e.fechaHoraEvento = :fecha AND e.fechaBajaEvento IS NULL")
    boolean existsByTituloAndFechaAndFechaBajaIsNull(@Param("titulo") String titulo, @Param("fecha") LocalDateTime fecha);

    // Métodos para búsqueda
    List<Evento> findByTituloEventoContainingIgnoreCaseOrDescripcionEventoContainingIgnoreCase(
            String tituloEvento, String descripcionEvento, Pageable pageable);
    
    // Buscar eventos por usuario en un período de tiempo
    @Query("SELECT e FROM Evento e WHERE e.idUsuario = :usuario AND e.fechaHoraEvento BETWEEN :fechaInicio AND :fechaFin AND e.fechaBajaEvento IS NULL")
    List<Evento> findByIdUsuarioAndFechaHoraEventoBetween(@Param("usuario") indie.models.moduloUsuario.Usuario usuario, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evento e WHERE e.tituloEvento = :titulo AND e.fechaHoraEvento = :fecha AND e.estadoEvento = :estado AND e.fechaBajaEvento IS NULL")
    boolean existsByTituloAndFechaAndEstadoEventoAndFechaBajaIsNull(@Param("titulo") String titulo, @Param("fecha") LocalDateTime fecha, @Param("estado") eventoEstado estado);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evento e WHERE e.tituloEvento = :titulo AND e.estadoEvento = :estado AND e.idUsuario.id = :idUsuario AND e.fechaBajaEvento IS NULL")
    boolean existsBorradorByTituloAndUsuario(@Param("titulo") String titulo, @Param("estado") eventoEstado estado, @Param("idUsuario") String idUsuario);
}
