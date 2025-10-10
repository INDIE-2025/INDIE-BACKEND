package indie.repositories.moduloReportes;

import indie.models.moduloReportes.VisualizacionPerfil;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisualizacionPerfilRepository extends JpaRepository<VisualizacionPerfil, Long> {
    
    List<VisualizacionPerfil> findByUsuarioVisitadoAndFechaVisualizacionBetweenOrderByFechaVisualizacionAsc(
            Usuario usuarioVisitado, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(v) FROM VisualizacionPerfil v WHERE v.usuarioVisitado = :usuario " +
           "AND v.fechaVisualizacion BETWEEN :fechaInicio AND :fechaFin")
    Long countVisualizacionesByUsuarioAndFechaBetween(
            @Param("usuario") Usuario usuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT DATE(v.fechaVisualizacion), COUNT(v) " +
           "FROM VisualizacionPerfil v WHERE v.usuarioVisitado = :usuario " +
           "AND v.fechaVisualizacion BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY DATE(v.fechaVisualizacion) " +
           "ORDER BY DATE(v.fechaVisualizacion)")
    List<Object[]> countVisualizacionesPorDia(
            @Param("usuario") Usuario usuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT MONTH(v.fechaVisualizacion), YEAR(v.fechaVisualizacion), COUNT(v) " +
           "FROM VisualizacionPerfil v WHERE v.usuarioVisitado = :usuario " +
           "AND v.fechaVisualizacion BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY YEAR(v.fechaVisualizacion), MONTH(v.fechaVisualizacion) " +
           "ORDER BY YEAR(v.fechaVisualizacion), MONTH(v.fechaVisualizacion)")
    List<Object[]> countVisualizacionesPorMes(
            @Param("usuario") Usuario usuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(v) FROM VisualizacionPerfil v WHERE v.usuarioVisitado.id = :usuarioId " +
           "AND v.fechaVisualizacion >= :fechaInicio AND v.fechaVisualizacion < :fechaFin")
    long contarVisualizacionesPorUsuarioYFecha(
            @Param("usuarioId") String usuarioId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}