package indie.repositories.moduloCalendario;

import indie.models.moduloCalendario.FechaCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface FechaCalendarioRepository extends JpaRepository<FechaCalendario, String> {
    
   
    
    // Buscar bloqueos activos por calendario
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL")
    List<FechaCalendario> findBloqueosByCalendario(@Param("calendarioId") String calendarioId);
    
    // Buscar bloqueos en un rango de fechas
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND ((fc.fechaDesde <= :fechaFin) AND (fc.fechaHasta >= :fechaInicio))")
    List<FechaCalendario> findBloqueosEnRango(
            @Param("calendarioId") String calendarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
    
    // Verificar si una fecha específica está bloqueada
    @Query("SELECT COUNT(fc) > 0 FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND :fecha BETWEEN fc.fechaDesde AND fc.fechaHasta")
    boolean isFechaBloqueada(
            @Param("calendarioId") String calendarioId,
            @Param("fecha") LocalDate fecha
    );
    
    // Verificar si una fecha y hora específica está bloqueada
    @Query("SELECT COUNT(fc) > 0 FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND :fecha BETWEEN fc.fechaDesde AND fc.fechaHasta " +
           "AND (:hora IS NULL OR fc.horaDesde IS NULL OR fc.horaHasta IS NULL OR " +
           "(:hora >= fc.horaDesde AND :hora <= fc.horaHasta))")
    boolean isFechaHoraBloqueada(
            @Param("calendarioId") String calendarioId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );
    
    // Buscar solapamientos con bloqueos existentes
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND fc.id != :excludeId " +
           "AND ((fc.fechaDesde <= :fechaFin) AND (fc.fechaHasta >= :fechaInicio))")
    List<FechaCalendario> findSolapamientosBloqueo(
            @Param("calendarioId") String calendarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("excludeId") String excludeId
    );
    
    // Buscar bloqueos por motivo
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND LOWER(fc.motivo) LIKE LOWER(CONCAT('%', :motivo, '%'))")
    List<FechaCalendario> findBloqueosByMotivo(
            @Param("calendarioId") String calendarioId,
            @Param("motivo") String motivo
    );
    
    // Buscar bloqueos futuros
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NULL AND fc.deletedAt IS NULL " +
           "AND fc.fechaHasta >= :fechaActual " +
           "ORDER BY fc.fechaDesde ASC")
    List<FechaCalendario> findBloqueosFuturos(
            @Param("calendarioId") String calendarioId,
            @Param("fechaActual") LocalDate fechaActual
    );
    
   
    
    // Buscar eventos activos por calendario
    @Query("SELECT fc FROM FechaCalendario fc WHERE fc.idCalendario.id = :calendarioId " +
           "AND fc.idEvento IS NOT NULL AND fc.deletedAt IS NULL")
    List<FechaCalendario> findEventosByCalendario(@Param("calendarioId") String calendarioId);
}