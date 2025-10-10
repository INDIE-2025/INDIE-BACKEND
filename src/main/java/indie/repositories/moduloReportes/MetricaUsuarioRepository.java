package indie.repositories.moduloReportes;

import indie.models.moduloReportes.MetricaUsuario;
import indie.models.moduloReportes.TipoMetrica;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricaUsuarioRepository extends JpaRepository<MetricaUsuario, Long> {
    
    List<MetricaUsuario> findByUsuarioAndTipoMetricaAndFechaMetricaBetweenOrderByFechaMetricaAsc(
            Usuario usuario, TipoMetrica tipoMetrica, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<MetricaUsuario> findByUsuarioAndFechaMetricaBetweenOrderByFechaMetricaAsc(
            Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT m FROM MetricaUsuario m WHERE m.usuario = :usuario " +
           "AND m.tipoMetrica.nombre = :nombreMetrica " +
           "AND m.fechaMetrica BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fechaMetrica ASC")
    List<MetricaUsuario> findByUsuarioAndTipoMetricaNombreAndFechaBetween(
            @Param("usuario") Usuario usuario,
            @Param("nombreMetrica") String nombreMetrica,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT SUM(m.valor) FROM MetricaUsuario m WHERE m.usuario = :usuario " +
           "AND m.tipoMetrica.nombre = :nombreMetrica " +
           "AND m.fechaMetrica BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumValorByUsuarioAndTipoMetricaAndFechaBetween(
            @Param("usuario") Usuario usuario,
            @Param("nombreMetrica") String nombreMetrica,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT m.periodoMes, m.periodoAnio, SUM(m.valor) " +
           "FROM MetricaUsuario m WHERE m.usuario = :usuario " +
           "AND m.tipoMetrica.nombre = :nombreMetrica " +
           "AND m.fechaMetrica BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY m.periodoAnio, m.periodoMes " +
           "ORDER BY m.periodoAnio, m.periodoMes")
    List<Object[]> findMetricasPorMes(
            @Param("usuario") Usuario usuario,
            @Param("nombreMetrica") String nombreMetrica,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT DATE(m.fechaMetrica), SUM(m.valor) " +
           "FROM MetricaUsuario m WHERE m.usuario = :usuario " +
           "AND m.tipoMetrica.nombre = :nombreMetrica " +
           "AND m.fechaMetrica BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY DATE(m.fechaMetrica) " +
           "ORDER BY DATE(m.fechaMetrica)")
    List<Object[]> findMetricasPorDia(
            @Param("usuario") Usuario usuario,
            @Param("nombreMetrica") String nombreMetrica,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}