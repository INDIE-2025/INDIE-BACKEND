package indie.repositories.moduloRecomendaciones;

import indie.models.moduloRecomendaciones.RecomendacionEventoDiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacionEventoDiariaRepository extends JpaRepository <RecomendacionEventoDiaria, String> {
}
