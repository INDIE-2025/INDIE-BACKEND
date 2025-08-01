package indie.services.moduloReportes;

import indie.models.moduloReportes.TipoMetrica;
import indie.repositories.moduloReportes.TipoMetricaRepository;
import indie.services.BaseServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class TipoMetricaService extends BaseServiceImpl<TipoMetrica, String> {
    
    TipoMetricaRepository tipoMetricaRepository;

    public TipoMetricaService(TipoMetricaRepository tipoMetricaRepository) {
        super(tipoMetricaRepository);
        this.tipoMetricaRepository = tipoMetricaRepository;
    }
}

