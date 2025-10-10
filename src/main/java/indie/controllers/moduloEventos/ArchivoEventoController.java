package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.models.moduloEventos.ArchivoEvento;
import indie.services.moduloEventos.ArchivoEventoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/archivoEvento")
public class ArchivoEventoController extends BaseController<ArchivoEvento, String> {

    @Autowired
    protected ArchivoEventoServiceImpl archivoEventoService;

    public ArchivoEventoController(ArchivoEventoServiceImpl archivoEventoService) {
        super(archivoEventoService);
    }
}

