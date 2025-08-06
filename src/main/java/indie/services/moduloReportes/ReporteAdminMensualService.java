package indie.services.moduloReportes;

import indie.models.moduloReportes.ReporteAdminMensual;
import indie.repositories.moduloReportes.ReporteAdminMensualRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ReporteAdminMensualService extends BaseServiceImpl<ReporteAdminMensual, String> {
    
    ReporteAdminMensualRepository reporteRepository;
 
    public ReporteAdminMensualService(ReporteAdminMensualRepository reporteRepository) {
        super(reporteRepository);
        this.reporteRepository = reporteRepository;
    }
    
}

