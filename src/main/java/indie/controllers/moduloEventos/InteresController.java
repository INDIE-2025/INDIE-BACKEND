package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.models.moduloEventos.Interes;
import indie.services.moduloEventos.InteresServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interes")
public class InteresController extends BaseController<Interes, String> {

    @Autowired
    protected InteresServiceImpl interesService;

    public InteresController(InteresServiceImpl interesService) {
        super(interesService);
    }
}
