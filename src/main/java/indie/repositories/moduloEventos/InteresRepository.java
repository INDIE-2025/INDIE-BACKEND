package indie.repositories.moduloEventos;

import indie.models.moduloEventos.Evento;
import indie.models.moduloEventos.Interes;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InteresRepository extends JpaRepository<Interes, String> {
    
    /**
     * Busca todos los intereses para un evento específico
     * @param evento El evento por el cual se buscan los intereses
     * @return Lista de intereses para el evento
     */
    List<Interes> findByIdEvento(Evento evento);
    
    /**
     * Busca todos los intereses de un usuario específico
     * @param usuario El usuario del cual se buscan los intereses
     * @return Lista de intereses del usuario
     */
    List<Interes> findByIdUsuario(Usuario usuario);
    
    /**
     * Busca un interés específico de un usuario en un evento
     * @param evento El evento de interés
     * @param usuario El usuario que puede estar interesado
     * @return El interés si existe, o empty si no existe
     */
    Optional<Interes> findByIdEventoAndIdUsuario(Evento evento, Usuario usuario);
    
    /**
     * Cuenta el número de interesados en un evento específico
     * @param idEvento ID del evento
     * @return Cantidad de interesados
     */
    @Query("SELECT COUNT(i) FROM Interes i WHERE i.idEvento.id = :idEvento AND i.fechaBajaInteres IS NULL")
    int countInteresadosPorEvento(@Param("idEvento") String idEvento);
}
