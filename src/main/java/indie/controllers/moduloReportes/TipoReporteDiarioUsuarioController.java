package indie.controllers.moduloReportes;

import indie.controllers.BaseController;
import indie.models.moduloReportes.TipoReporteDiarioUsuario;
import indie.services.moduloReportes.TipoMetricaService;
import indie.services.moduloReportes.TipoReporteDiarioUsuarioService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reportes-diarios-usuario")
@CrossOrigin(origins = "*")
public class TipoReporteDiarioUsuarioController extends BaseController<TipoReporteDiarioUsuario, String> {
    
    TipoReporteDiarioUsuarioService reporteUsuarioService;
    TipoMetricaService tipoMetricaService;
    
    public TipoReporteDiarioUsuarioController(
        TipoReporteDiarioUsuarioService reporteUsuarioService, TipoMetricaService tipoMetricaService) {
        super(reporteUsuarioService);
        this.reporteUsuarioService = reporteUsuarioService;
        this.tipoMetricaService = tipoMetricaService;
    }
 
}
