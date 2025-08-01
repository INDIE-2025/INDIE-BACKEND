package indie.services;

import indie.models.moduloReportes.TipoMetrica;
import indie.repositories.TipoMetricaRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoMetricaService extends BaseServiceImpl<TipoMetrica, String> {
    
    TipoMetricaRepository tipoMetricaRepository;

    public TipoMetricaService(TipoMetricaRepository tipoMetricaRepository) {
        super(tipoMetricaRepository);
        this.tipoMetricaRepository = tipoMetricaRepository;
    }
}

