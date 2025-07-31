package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.models.moduloEventos.Evento;
import indie.services.moduloEventos.EventoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evento")
public class EventoController extends BaseController<Evento, String> {

    protected EventoServiceImpl eventoService;

    public EventoController(EventoServiceImpl eventoService) {
        super(eventoService);
    }
}
