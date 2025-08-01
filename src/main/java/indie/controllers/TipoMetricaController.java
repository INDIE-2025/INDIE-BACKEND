package indie.controllers;

import indie.models.moduloReportes.TipoMetrica;
import indie.services.TipoMetricaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/tipos-metrica")
@CrossOrigin(origins = "*")
public class TipoMetricaController extends BaseController<TipoMetrica, String> {
    
    @Autowired
    TipoMetricaService tipoMetricaService;
    
    public TipoMetricaController(TipoMetricaService tipoMetricaService) {
        super(tipoMetricaService);
        this.tipoMetricaService = tipoMetricaService;
    }
}

