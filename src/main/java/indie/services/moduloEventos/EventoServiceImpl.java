package indie.services.moduloEventos;

import indie.models.moduloEventos.Evento;
import indie.repositories.moduloEventos.EventoRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, String> implements EventoService {

    EventoRepository eventoRepository;

    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
    }
}
