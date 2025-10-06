package indie.services.moduloCalendario;

import indie.models.moduloCalendario.Calendario;
import indie.repositories.moduloCalendario.CalendarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarioServiceImpl extends BaseServiceImpl<Calendario,String> implements CalendarioService{
    CalendarioRepository calendarioRepository;

    @Autowired
    public CalendarioServiceImpl(CalendarioRepository calendarioRepository) {
        super(calendarioRepository);
        this.calendarioRepository = calendarioRepository;
    }

    /**
     * Buscar calendario por ID de usuario
     */
    public Optional<Calendario> findByUsuarioId(String usuarioId) {
        return calendarioRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Buscar calendario por username (MÁS AMIGABLE) 
     */
    public Optional<Calendario> findByUsername(String username) {
        return calendarioRepository.findByUsername(username);
    }
}
