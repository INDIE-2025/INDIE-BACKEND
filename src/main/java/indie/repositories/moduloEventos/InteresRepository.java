package indie.repositories.moduloEventos;

import indie.models.moduloEventos.Interes;
import indie.models.moduloEventos.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InteresRepository extends JpaRepository<Interes, String> {
    
    // Contar usuarios interesados en un evento (solo intereses activos)
    @Query("SELECT COUNT(i) FROM Interes i WHERE i.idEvento = :evento AND i.fechaBajaInteres IS NULL")
    long countByEvento(@Param("evento") Evento evento);

    @Query("SELECT COUNT(i) FROM Interes i JOIN i.idEvento e WHERE e.idUsuario.id = :usuarioId " +
           "AND i.fechaAltaInteres >= :fechaInicio AND i.fechaAltaInteres < :fechaFin")
    long contarInteresesPorUsuarioYFecha(
            @Param("usuarioId") String usuarioId,
            @Param("fechaInicio") java.time.LocalDateTime fechaInicio,
            @Param("fechaFin") java.time.LocalDateTime fechaFin);
}
