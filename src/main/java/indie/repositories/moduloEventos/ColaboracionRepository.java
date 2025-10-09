package indie.repositories.moduloEventos;

import indie.models.moduloEventos.Colaboracion;
import indie.models.moduloEventos.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ColaboracionRepository extends JpaRepository<Colaboracion, String> {
    
    /**
     * Encuentra todas las colaboraciones asociadas a un evento
     * @param evento El evento para el cual buscar colaboraciones
     * @return Lista de colaboraciones del evento
     */
    List<Colaboracion> findByIdEvento(Evento evento);
    
    /**
     * Encuentra todas las colaboraciones asociadas a un ID de evento
     * @param eventoId El ID del evento para el cual buscar colaboraciones
     * @return Lista de colaboraciones del evento
     */
    @Query("SELECT c FROM Colaboracion c WHERE c.idEvento.id = :eventoId")
    List<Colaboracion> findByEventoId(@Param("eventoId") String eventoId);
    
    /**
     * Elimina todas las colaboraciones asociadas a un evento
     * @param evento El evento del cual eliminar colaboraciones
     */
    void deleteByIdEvento(Evento evento);
}
