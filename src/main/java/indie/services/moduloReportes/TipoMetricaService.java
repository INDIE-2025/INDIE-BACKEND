package indie.services.moduloReportes;

import indie.models.moduloReportes.TipoMetrica;
import indie.repositories.moduloReportes.TipoMetricaRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TipoMetricaService extends BaseServiceImpl<TipoMetrica, String> {
    private final TipoMetricaRepository repo;

    public TipoMetricaService(TipoMetricaRepository repo) {
        super(repo);
        this.repo = repo;
    }
}

