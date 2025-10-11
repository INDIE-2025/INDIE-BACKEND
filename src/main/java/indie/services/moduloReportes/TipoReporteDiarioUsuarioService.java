package indie.services.moduloReportes;

import indie.models.moduloReportes.TipoReporteDiarioUsuario;
import indie.repositories.moduloReportes.TipoReporteDiarioUsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TipoReporteDiarioUsuarioService extends BaseServiceImpl<TipoReporteDiarioUsuario, String> {
    private final TipoReporteDiarioUsuarioRepository repo;

    public TipoReporteDiarioUsuarioService(TipoReporteDiarioUsuarioRepository repo) {
        super(repo);
        this.repo = repo;
    }
}

