package indie.services;

import indie.models.moduloReportes.ReporteAdminMensual;
import indie.repositories.ReporteAdminMensualRepository;
import org.springframework.stereotype.Service;

@Service
public class ReporteAdminMensualService extends BaseServiceImpl<ReporteAdminMensual, String> {
    
    ReporteAdminMensualRepository reporteRepository;
 
    public ReporteAdminMensualService(ReporteAdminMensualRepository reporteRepository) {
        super(reporteRepository);
        this.reporteRepository = reporteRepository;
    }
    
}

