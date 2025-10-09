package indie.services.moduloEventos;

import indie.models.moduloEventos.Interes;
import indie.services.BaseService;

public interface InteresService extends BaseService<Interes, String> {
    
    /**
     * Marca un evento como interesante para un usuario
     * @param idEvento ID del evento
     * @param idUsuario ID del usuario
     * @return true si se pudo marcar el interés, false si ya estaba marcado
     */
    boolean marcarInteresEnEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                                  @org.springframework.data.repository.query.Param("idUsuario") String idUsuario);
    
    /**
     * Quita el interés de un usuario en un evento
     * @param idEvento ID del evento
     * @param idUsuario ID del usuario
     * @return true si se pudo quitar el interés, false si no estaba interesado
     */
    boolean quitarInteresEnEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                                 @org.springframework.data.repository.query.Param("idUsuario") String idUsuario);
    
    /**
     * Verifica si un usuario está interesado en un evento
     * @param idEvento ID del evento
     * @param idUsuario ID del usuario
     * @return true si está interesado, false si no
     */
    boolean estaInteresado(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                          @org.springframework.data.repository.query.Param("idUsuario") String idUsuario);
    
    /**
     * Cuenta el número de usuarios interesados en un evento
     * @param idEvento ID del evento
     * @return Número de usuarios interesados
     */
    int contarInteresadosPorEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento);
}
