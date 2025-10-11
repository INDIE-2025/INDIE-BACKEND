package indie.controllers.moduloReportes;

import indie.controllers.BaseController;
import indie.models.moduloReportes.TipoMetrica;
import indie.services.moduloReportes.TipoMetricaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tipos-metrica")
public class TipoMetricaController extends BaseController<TipoMetrica, String> {
    public TipoMetricaController(TipoMetricaService service) {
        super(service);
    }
}

