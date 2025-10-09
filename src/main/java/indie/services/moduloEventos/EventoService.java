package indie.services.moduloEventos;

import indie.dtos.moduloEventos.EventoResponse;
import indie.dtos.moduloEventos.crearEventoDTO;
import indie.models.moduloEventos.Evento;
import indie.services.BaseService;
import java.util.List;

public interface EventoService extends BaseService<Evento, String> {

    EventoResponse crear(crearEventoDTO r);
    
    EventoResponse actualizar(String id, crearEventoDTO r);

    EventoResponse guardarBorrador(crearEventoDTO r);

    List<Evento> obtenerBorradoresPorUsuario(String idUsuario);
    
    void marcarBorradorComoBaja(String titulo, String idUsuario);
    
    boolean existeEventoConTituloYFecha(String titulo, java.time.LocalDateTime fecha);

    Evento findById(String idUsuario);
    
    List<Evento> obtenerPublicadosPorUsuario(String idUsuario);
}
