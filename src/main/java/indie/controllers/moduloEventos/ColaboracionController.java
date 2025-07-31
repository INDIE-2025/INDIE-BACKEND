package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.models.moduloEventos.Colaboracion;
import indie.services.moduloEventos.ColaboracionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colaboracion")
public class ColaboracionController extends BaseController<Colaboracion, String> {

    @Autowired
    protected ColaboracionServiceImpl colaboracionService;

    public ColaboracionController(ColaboracionServiceImpl colaboracionService) {
        super(colaboracionService);
    }
}
