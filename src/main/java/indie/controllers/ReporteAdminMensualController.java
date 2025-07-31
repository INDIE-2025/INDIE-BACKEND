package indie.controllers;

import indie.models.moduloReportes.ReporteAdminMensual;
import indie.services.ReporteAdminMensualService;
import indie.services.TipoMetricaService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/reportes-mensuales")
@CrossOrigin(origins = "*")
public class ReporteAdminMensualController extends BaseController<ReporteAdminMensual, String> {
    
    ReporteAdminMensualService reporteService;
    TipoMetricaService tipoMetricaService;
    
    public ReporteAdminMensualController(ReporteAdminMensualService reporteService,
                                         TipoMetricaService tipoMetricaService) {
        super(reporteService);
        this.reporteService = reporteService;
        this.tipoMetricaService = tipoMetricaService;
    }
 
}

