package indie.controllers.moduloReportes;

import indie.controllers.BaseController;
import indie.models.moduloReportes.ReporteAdminMensual;
import indie.services.moduloReportes.ReporteAdminMensualService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reportes-mensuales")
public class ReporteAdminMensualController extends BaseController<ReporteAdminMensual, String> {
    public ReporteAdminMensualController(ReporteAdminMensualService service) {
        super(service);
    }
}

