package indie.controllers.moduloReportes;

import indie.controllers.BaseController;
import indie.models.moduloReportes.ReporteAdminMensual;
import indie.services.moduloReportes.ReporteAdminMensualService;
import indie.services.moduloReportes.TipoMetricaService;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/reportes-mensuales")
@CrossOrigin(origins = "*")
public class ReporteAdminMensualController extends BaseController<ReporteAdminMensual, String> {
    
    ReporteAdminMensualService reporteAdminService;
    TipoMetricaService tipoMetricaService;
    
    public ReporteAdminMensualController(ReporteAdminMensualService reporteAdminService,
                                         TipoMetricaService tipoMetricaService) {
        super(reporteAdminService);
        this.reporteAdminService = reporteAdminService;
        this.tipoMetricaService = tipoMetricaService;
    }
 
}

