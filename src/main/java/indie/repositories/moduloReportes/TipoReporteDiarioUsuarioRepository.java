package indie.repositories.moduloReportes;

import indie.models.moduloReportes.TipoReporteDiarioUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoReporteDiarioUsuarioRepository extends JpaRepository<TipoReporteDiarioUsuario, String> {
    
}
