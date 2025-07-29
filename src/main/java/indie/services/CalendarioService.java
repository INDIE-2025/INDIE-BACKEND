package indie.services;

import indie.models.moduloCalendario.Calendario;
import indie.repositories.CalendarioRepository;
import indie.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CalendarioService extends BaseServiceImpl<Calendario,Long> {
    CalendarioRepository calendarioRepository;

    @Autowired
    public CalendarioService(CalendarioRepository calendarioRepository) {
        super(calendarioRepository);
        this.calendarioRepository = calendarioRepository;
    }
}
