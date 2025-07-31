package indie.controllers.moduloRecomendaciones;

import indie.controllers.BaseController;
import indie.models.moduloRecomendaciones.RecomendacionEventoDiaria;
import indie.services.moduloRecomendaciones.RecomendacionEventoDiariaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacionEventoDiaria")
public class RecomendacionEventoDiariaController extends BaseController<RecomendacionEventoDiaria, String> {

    public RecomendacionEventoDiariaController(RecomendacionEventoDiariaService recomendacionEventoDiariaService) {
        super(recomendacionEventoDiariaService);
    }
}
