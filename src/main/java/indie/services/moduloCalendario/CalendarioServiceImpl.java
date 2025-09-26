package indie.services.moduloCalendario;

import indie.models.moduloCalendario.Calendario;
import indie.repositories.moduloCalendario.CalendarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CalendarioServiceImpl extends BaseServiceImpl<Calendario,String> implements CalendarioService{
    CalendarioRepository calendarioRepository;

    public CalendarioServiceImpl(CalendarioRepository calendarioRepository) {
        super(calendarioRepository);
        this.calendarioRepository = calendarioRepository;
    }
}
