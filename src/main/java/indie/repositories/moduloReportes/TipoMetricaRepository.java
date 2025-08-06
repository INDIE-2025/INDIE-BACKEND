package indie.repositories.moduloReportes;

import indie.models.moduloReportes.TipoMetrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TipoMetricaRepository extends JpaRepository<TipoMetrica, String> {
    
}

