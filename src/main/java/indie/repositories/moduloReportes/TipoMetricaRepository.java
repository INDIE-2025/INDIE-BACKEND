package indie.repositories.moduloReportes;

import indie.models.moduloReportes.TipoMetrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMetricaRepository extends JpaRepository<TipoMetrica, String> {
    
    boolean existsByNombre(String nombre);
    
    Optional<TipoMetrica> findByNombre(String nombre);
}

