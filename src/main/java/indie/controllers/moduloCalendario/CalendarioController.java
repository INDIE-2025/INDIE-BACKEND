package indie.controllers.moduloCalendario;

import indie.controllers.BaseController;
import indie.models.moduloCalendario.Calendario;
import indie.services.moduloCalendario.CalendarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioController extends BaseController<Calendario,Long> {

    public CalendarioController(CalendarioService calendarioService) {
        super(calendarioService);
    }

}
