package indie.controllers.moduloReportes;

import indie.controllers.BaseController;
import indie.models.moduloReportes.TipoReporteDiarioUsuario;
import indie.services.moduloReportes.TipoReporteDiarioUsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reportes-diarios-usuario")
public class TipoReporteDiarioUsuarioController extends BaseController<TipoReporteDiarioUsuario, String> {
    public TipoReporteDiarioUsuarioController(TipoReporteDiarioUsuarioService service) {
        super(service);
    }
}

