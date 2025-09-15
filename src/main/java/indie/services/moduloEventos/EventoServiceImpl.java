package indie.services.moduloEventos;

import indie.dtos.moduloEventos.crearEventoDTO;
import indie.models.moduloEventos.Evento;
import indie.models.moduloUsuario.Usuario;
import indie.dtos.moduloEventos.EventoResponse;
import indie.repositories.moduloEventos.EventoRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, String> implements EventoService {

    EventoRepository eventoRepository;
    UsuarioRepository usuarioRepository;

    public EventoServiceImpl(EventoRepository eventoRepository, UsuarioRepository usuarioRepository) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EventoResponse crear(crearEventoDTO r){
        if (r.fechaHoraEvento.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede ser en el pasado");
        }
        
        // Verificar si ya existe un evento con el mismo título y fecha
        if (existeEventoConTituloYFecha(r.titulo, r.fechaHoraEvento)) {
            throw new indie.exceptions.EventoDuplicadoException("Ya existe un evento con el mismo título y fecha");
        }

        Usuario creador = usuarioRepository.findById(r.idUsuario)
        .orElseThrow(() -> new IllegalArgumentException("Creador inexistente"));
        
        // Si estamos publicando desde un borrador, marcar el borrador como dado de baja
        if (r.estadoEvento != null && r.estadoEvento.equals("PUBLICADO")) {
            marcarBorradorComoBaja(r.titulo, r.idUsuario);
        }

        Evento e = new Evento();
        e.setTituloEvento(r.titulo);
        e.setDescripcionEvento(r.descripcion);
        e.setFechaHoraEvento(r.fechaHoraEvento);
        e.setFechaAltaEvento(LocalDateTime.now());
        e.setUbicacionEvento(r.ubicacion);
        e.setIdUsuario(creador);
        if (r.estadoEvento != null) {
            e.setEstadoEvento(indie.models.enums.eventoEstado.valueOf(r.estadoEvento));
        } else {
            e.setEstadoEvento(indie.models.enums.eventoEstado.PUBLICADO);
        }

        Evento guardado = eventoRepository.save(e);

        EventoResponse out = new EventoResponse();
        out.id = guardado.getId();
        out.titulo = guardado.getTituloEvento();
        out.descripcion = guardado.getDescripcionEvento();
        out.fechaHoraEvento = guardado.getFechaHoraEvento();
        out.ubicacion = guardado.getUbicacionEvento();
        out.idUsuario = guardado.getIdUsuario().getId();
        out.createdAt = guardado.getFechaAltaEvento();
        out.updatedAt = guardado.getFechaModificacionEvento();

        return out;
    }

    @Transactional
    public EventoResponse guardarBorrador(crearEventoDTO r){
        Usuario creador = usuarioRepository.findById(r.idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Creador inexistente"));

        Evento e = new Evento();
        e.setTituloEvento(r.titulo);
        e.setDescripcionEvento(r.descripcion);
        e.setFechaHoraEvento(r.fechaHoraEvento);
        e.setFechaAltaEvento(LocalDateTime.now());
        e.setUbicacionEvento(r.ubicacion);
        e.setIdUsuario(creador);
        if (r.estadoEvento != null) {
            e.setEstadoEvento(indie.models.enums.eventoEstado.valueOf(r.estadoEvento));
        } else {
            e.setEstadoEvento(indie.models.enums.eventoEstado.BORRADOR);
        }

        Evento guardado = eventoRepository.save(e);

        EventoResponse out = new EventoResponse();
        out.id = guardado.getId();
        out.titulo = guardado.getTituloEvento();
        out.descripcion = guardado.getDescripcionEvento();
        out.fechaHoraEvento = guardado.getFechaHoraEvento();
        out.ubicacion = guardado.getUbicacionEvento();
        out.idUsuario = guardado.getIdUsuario().getId();
        out.createdAt = guardado.getFechaAltaEvento();
        out.updatedAt = guardado.getFechaModificacionEvento();

        return out;
    }
    
    public List<Evento> obtenerBorradoresPorUsuario(String idUsuario){
        return eventoRepository.findByEstadoEventoAndIdUsuario(indie.models.enums.eventoEstado.BORRADOR, idUsuario);
    }
    
    @Transactional
    public void marcarBorradorComoBaja(String titulo, String idUsuario) {
        Optional<Evento> borrador = eventoRepository.findByTituloAndEstadoAndIdUsuario(
            titulo, 
            indie.models.enums.eventoEstado.BORRADOR, 
            idUsuario
        );
        
        borrador.ifPresent(e -> {
            e.setFechaBajaEvento(LocalDateTime.now());
            eventoRepository.save(e);
        });
    }
    
    public boolean existeEventoConTituloYFecha(String titulo, LocalDateTime fecha) {
        return eventoRepository.existsByTituloAndFechaAndFechaBajaIsNull(titulo, fecha);
    }

    @Override
    public Evento findById(String id) {
        return eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
    }
}
