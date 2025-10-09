package indie.services.moduloEventos;

import indie.models.moduloEventos.Evento;
import indie.models.moduloEventos.Interes;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloEventos.EventoRepository;
import indie.repositories.moduloEventos.InteresRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class InteresServiceImpl extends BaseServiceImpl<Interes, String> implements InteresService {

    InteresRepository interesRepository;
    EventoRepository eventoRepository;
    UsuarioRepository usuarioRepository;

    public InteresServiceImpl(
        InteresRepository interesRepository, 
        EventoRepository eventoRepository,
        UsuarioRepository usuarioRepository
    ) {
        super(interesRepository);
        this.interesRepository = interesRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public boolean marcarInteresEnEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                                         @org.springframework.data.repository.query.Param("idUsuario") String idUsuario) {
        // Validar que el evento y el usuario existan
        Evento evento = eventoRepository.findById(idEvento)
            .orElseThrow(() -> new IllegalArgumentException("El evento no existe"));
        
        // Solo permitir marcar interés en eventos publicados
        if (evento.getEstadoEvento() != indie.models.enums.eventoEstado.PUBLICADO) {
            throw new IllegalArgumentException("Solo se puede marcar interés en eventos publicados");
        }
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        
        // Verificar si ya existe un interés
        Optional<Interes> interesExistente = interesRepository.findByIdEventoAndIdUsuario(evento, usuario);
        
        if (interesExistente.isPresent()) {
            Interes interes = interesExistente.get();
            // Si el interés estaba dado de baja, lo reactivamos
            if (interes.getFechaBajaInteres() != null) {
                interes.setFechaBajaInteres(null);
                interesRepository.save(interes);
                return true;
            }
            return false; // Ya estaba interesado
        }
        
        // Crear nuevo interés
        Interes nuevoInteres = new Interes();
        nuevoInteres.setIdEvento(evento);
        nuevoInteres.setIdUsuario(usuario);
        nuevoInteres.setFechaAltaInteres(new Date());
        nuevoInteres.setFechaBajaInteres(null);
        
        interesRepository.save(nuevoInteres);
        return true;
    }

    @Override
    @Transactional
    public boolean quitarInteresEnEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                                        @org.springframework.data.repository.query.Param("idUsuario") String idUsuario) {
        // Validar que el evento y el usuario existan
        Evento evento = eventoRepository.findById(idEvento)
            .orElseThrow(() -> new IllegalArgumentException("El evento no existe"));
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        
        // Verificar si existe un interés activo
        Optional<Interes> interesExistente = interesRepository.findByIdEventoAndIdUsuario(evento, usuario);
        
        if (interesExistente.isPresent()) {
            Interes interes = interesExistente.get();
            // Si el interés no estaba dado de baja, lo marcamos como inactivo
            if (interes.getFechaBajaInteres() == null) {
                interes.setFechaBajaInteres(new Date());
                interesRepository.save(interes);
                return true;
            }
        }
        
        return false; // No estaba interesado
    }

    @Override
    public boolean estaInteresado(@org.springframework.data.repository.query.Param("idEvento") String idEvento, 
                                 @org.springframework.data.repository.query.Param("idUsuario") String idUsuario) {
        try {
            Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("El evento no existe"));
            
            Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
            
            Optional<Interes> interes = interesRepository.findByIdEventoAndIdUsuario(evento, usuario);
            
            return interes.isPresent() && interes.get().getFechaBajaInteres() == null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int contarInteresadosPorEvento(@org.springframework.data.repository.query.Param("idEvento") String idEvento) {
        return interesRepository.countInteresadosPorEvento(idEvento);
    }
}
