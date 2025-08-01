package indie.repositories;

import indie.models.moduloReportes.ReporteAdminMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteAdminMensualRepository extends JpaRepository<ReporteAdminMensual, String> {
    
}

