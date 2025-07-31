package indie.services.moduloRecomendaciones;

import indie.models.moduloRecomendaciones.RecomendacionEventoDiaria;
import indie.repositories.moduloRecomendaciones.RecomendacionEventoDiariaRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RecomendacionEventoDiariaService extends BaseServiceImpl<RecomendacionEventoDiaria, String> {

    RecomendacionEventoDiariaRepository recomendacionEventoDiariaRepository;

    public RecomendacionEventoDiariaService(RecomendacionEventoDiariaRepository recomendacionEventoDiariaRepository) {
        super(recomendacionEventoDiariaRepository);
        this.recomendacionEventoDiariaRepository = recomendacionEventoDiariaRepository;
    }
}
