package indie.services.moduloEventos;

import indie.dtos.moduloEventos.crearEventoDTO;
import indie.models.enums.EstadoColaboracion;
import indie.models.moduloEventos.Colaboracion;
import indie.models.moduloEventos.Evento;
import indie.models.moduloUsuario.Usuario;
import indie.models.moduloCalendario.Calendario;
import indie.models.moduloCalendario.FechaCalendario;
import indie.dtos.moduloEventos.EventoResponse;
import indie.repositories.moduloEventos.ColaboracionRepository;
import indie.repositories.moduloCalendario.FechaCalendarioRepository;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import indie.services.moduloCalendario.CalendarioServiceImpl;
import indie.repositories.moduloEventos.EventoRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, String> implements EventoService {

    EventoRepository eventoRepository;
    UsuarioRepository usuarioRepository;
    ColaboracionRepository colaboracionRepository;
    FechaCalendarioRepository fechaCalendarioRepository;
    CalendarioServiceImpl calendarioService;
    NotificacionServiceImpl notificacionService;

    public EventoServiceImpl(EventoRepository eventoRepository, 
                             UsuarioRepository usuarioRepository,
                             ColaboracionRepository colaboracionRepository,
                             FechaCalendarioRepository fechaCalendarioRepository,
                             CalendarioServiceImpl calendarioService,
                             NotificacionServiceImpl notificacionService) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.colaboracionRepository = colaboracionRepository;
        this.fechaCalendarioRepository = fechaCalendarioRepository;
        this.calendarioService = calendarioService;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public EventoResponse crear(crearEventoDTO r){
        // Validación del título
        if (r.titulo == null || r.titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        
        // Validación de la fecha
        if (r.fechaHoraEvento == null) {
            throw new IllegalArgumentException("La fecha y hora del evento son obligatorias");
        }
        
        if (r.fechaHoraEvento.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede ser en el pasado");
        }
        
        // Verificar si ya existe un evento PUBLICADO con el mismo título y fecha
        // Solo validamos duplicados para eventos PUBLICADOS, no para borradores
        if (r.estadoEvento == null || r.estadoEvento.equals("PUBLICADO")) {
            if (existeEventoConTituloYFecha(r.titulo, r.fechaHoraEvento)) {
                throw new indie.exceptions.EventoDuplicadoException("Ya existe un evento publicado con el mismo título y fecha");
            }
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
        
        // Agregar evento al calendario si está publicado
        if (guardado.getEstadoEvento() == indie.models.enums.eventoEstado.PUBLICADO) {
            agregarEventoAlCalendario(guardado);
        }
        
        // Procesar colaboradores si están presentes
        List<String> colaboradoresIds = new ArrayList<>();
        if (r.colaboradores != null && !r.colaboradores.isEmpty()) {
            System.out.println("Processing " + r.colaboradores.size() + " collaborators for event: " + guardado.getId());
            colaboradoresIds = r.colaboradores;
            guardarColaboradores(guardado, colaboradoresIds);
        }

        EventoResponse out = new EventoResponse();
        out.id = guardado.getId();
        out.titulo = guardado.getTituloEvento();
        out.descripcion = guardado.getDescripcionEvento();
        out.fechaHoraEvento = guardado.getFechaHoraEvento();
        out.ubicacion = guardado.getUbicacionEvento();
        out.idUsuario = guardado.getIdUsuario().getId();
        out.createdAt = guardado.getFechaAltaEvento();
        out.updatedAt = guardado.getFechaModificacionEvento();
        out.colaboradoresIds = colaboradoresIds; // Incluir los IDs de colaboradores en la respuesta

        return out;
    }
    
    @Transactional
    public EventoResponse actualizar(String id, crearEventoDTO r){
        // Validación del título
        if (r.titulo == null || r.titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        
        // Validación de la fecha
        if (r.fechaHoraEvento == null) {
            throw new IllegalArgumentException("La fecha y hora del evento son obligatorias");
        }
        
        if (r.fechaHoraEvento.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede ser en el pasado");
        }
        
        // Buscar el evento existente
        Evento existente = eventoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
            
        // Verificar que el usuario que actualiza sea el creador del evento
        if (!existente.getIdUsuario().getId().equals(r.idUsuario)) {
            throw new IllegalArgumentException("No tienes permiso para modificar este evento");
        }
        
        // Verificar duplicados solo si cambia el título o la fecha
        if (r.estadoEvento == null || r.estadoEvento.equals("PUBLICADO")) {
            boolean tituloModificado = !existente.getTituloEvento().equals(r.titulo);
            boolean fechaModificada = !existente.getFechaHoraEvento().equals(r.fechaHoraEvento);
            
            if ((tituloModificado || fechaModificada) && 
                existeEventoConTituloYFecha(r.titulo, r.fechaHoraEvento) && 
                !existente.getId().equals(id)) {
                throw new indie.exceptions.EventoDuplicadoException("Ya existe un evento publicado con el mismo título y fecha");
            }
        }
        
        // Actualizar los datos del evento
        existente.setTituloEvento(r.titulo);
        existente.setDescripcionEvento(r.descripcion);
        existente.setFechaHoraEvento(r.fechaHoraEvento);
        existente.setFechaModificacionEvento(LocalDateTime.now());
        existente.setUbicacionEvento(r.ubicacion);
        
        if (r.estadoEvento != null) {
            existente.setEstadoEvento(indie.models.enums.eventoEstado.valueOf(r.estadoEvento));
        }

        Evento guardado = eventoRepository.save(existente);
        
        // Agregar evento al calendario si está publicado
        if (guardado.getEstadoEvento() == indie.models.enums.eventoEstado.PUBLICADO) {
            agregarEventoAlCalendario(guardado);
        }
        
        // Procesar colaboradores si están presentes
        List<String> colaboradoresIds = new ArrayList<>();
        if (r.colaboradores != null) {
            System.out.println("Processing " + r.colaboradores.size() + " collaborators for event update: " + guardado.getId());
            colaboradoresIds = r.colaboradores;
            guardarColaboradores(guardado, colaboradoresIds);
        }

        EventoResponse out = new EventoResponse();
        out.id = guardado.getId();
        out.titulo = guardado.getTituloEvento();
        out.descripcion = guardado.getDescripcionEvento();
        out.fechaHoraEvento = guardado.getFechaHoraEvento();
        out.ubicacion = guardado.getUbicacionEvento();
        out.idUsuario = guardado.getIdUsuario().getId();
        out.createdAt = guardado.getFechaAltaEvento();
        out.updatedAt = guardado.getFechaModificacionEvento();
        out.colaboradoresIds = colaboradoresIds; // Incluir los IDs de colaboradores en la respuesta

        return out;
    }

    @Transactional
    public EventoResponse guardarBorrador(crearEventoDTO r){
        // Debug log to track what's coming in
        System.out.println("guardarBorrador - Input: id=" + r.id + ", title=" + r.titulo 
                + ", fechaHoraEvento=" + (r.fechaHoraEvento != null ? r.fechaHoraEvento.toString() : "null"));
        
        if (r.titulo == null || r.titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        
        // No date/time validation for drafts - they can be saved with incomplete information
        
        Usuario creador = usuarioRepository.findById(r.idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Creador inexistente"));
        
        Evento e;
        
        // Verificamos si estamos actualizando un borrador existente
        if (r.id != null && !r.id.trim().isEmpty()) {
            System.out.println("Attempting to update existing draft with ID: " + r.id);
            
            // Buscamos el borrador existente
            Optional<Evento> existingBorrador = eventoRepository.findById(r.id);
            
            if (existingBorrador.isPresent() && existingBorrador.get().getEstadoEvento() == indie.models.enums.eventoEstado.BORRADOR) {
                // Verificamos que el borrador pertenezca al usuario que lo intenta actualizar
                if (!existingBorrador.get().getIdUsuario().getId().equals(r.idUsuario)) {
                    throw new IllegalArgumentException("No tienes permiso para modificar este borrador");
                }
                
                e = existingBorrador.get();
                System.out.println("Updating existing draft: " + e.getId() + " - " + e.getTituloEvento());
            } else {
                // Si no existe, creamos uno nuevo
                System.out.println("Draft with ID " + r.id + " not found, creating new draft");
                e = new Evento();
                e.setFechaAltaEvento(LocalDateTime.now());
            }
        } else {
            // Es un borrador nuevo
            System.out.println("Creating completely new draft");
            e = new Evento();
            e.setFechaAltaEvento(LocalDateTime.now());
            
            // Only check for duplicates for new drafts without ID
            // This allows users to create multiple drafts with different content
            Optional<Evento> existingBorradorByTitle = eventoRepository.findByTituloAndEstadoAndIdUsuario(
                r.titulo, 
                indie.models.enums.eventoEstado.BORRADOR, 
                r.idUsuario
            );
            
            if (existingBorradorByTitle.isPresent()) {
                System.out.println("Found existing draft with same title: " + existingBorradorByTitle.get().getId());
                // Instead of error, we could append a unique identifier or timestamp to make title unique
                // Or simply add a version number: "My Event (2)"
                throw new IllegalArgumentException("Ya tienes un borrador con el título '" + r.titulo + 
                    "'. Por favor, selecciona ese borrador de la lista para editarlo o usa un título diferente.");
            }
        }
        
        // Actualizamos los datos del borrador
        e.setTituloEvento(r.titulo);
        e.setDescripcionEvento(r.descripcion);
        
        // Handle the date/time field for drafts
        // For drafts, we accept incomplete information - fechaHoraEvento can be null
        // The frontend now sends a value even if only date or only time was provided
        e.setFechaHoraEvento(r.fechaHoraEvento);
        System.out.println("Setting fechaHoraEvento: " + (r.fechaHoraEvento != null ? r.fechaHoraEvento.toString() : "null"));
        
        e.setFechaModificacionEvento(LocalDateTime.now()); // Always update modification time
        e.setUbicacionEvento(r.ubicacion);
        e.setIdUsuario(creador);
        if (r.estadoEvento != null) {
            e.setEstadoEvento(indie.models.enums.eventoEstado.valueOf(r.estadoEvento));
        } else {
            e.setEstadoEvento(indie.models.enums.eventoEstado.BORRADOR);
        }

        Evento guardado = eventoRepository.save(e);
        
        // Agregar evento al calendario si cambió a PUBLICADO
        if (guardado.getEstadoEvento() == indie.models.enums.eventoEstado.PUBLICADO) {
            agregarEventoAlCalendario(guardado);
        }
        
        // Log the saved entity for debugging
        System.out.println("Draft saved successfully - ID: " + guardado.getId() + 
                          ", Title: " + guardado.getTituloEvento() + 
                          ", Date/Time: " + (guardado.getFechaHoraEvento() != null ? 
                                            guardado.getFechaHoraEvento().toString() : "null"));
        
        // Procesar colaboradores si están presentes
        List<String> colaboradoresIds = new ArrayList<>();
        if (r.colaboradores != null && !r.colaboradores.isEmpty()) {
            System.out.println("Processing " + r.colaboradores.size() + " collaborators for draft: " + guardado.getId());
            colaboradoresIds = r.colaboradores;
            guardarColaboradores(guardado, colaboradoresIds);
        } else {
            System.out.println("No collaborators provided for draft: " + guardado.getId());
        }

        EventoResponse out = new EventoResponse();
        out.id = guardado.getId();
        out.titulo = guardado.getTituloEvento();
        out.descripcion = guardado.getDescripcionEvento();
        out.fechaHoraEvento = guardado.getFechaHoraEvento();
        out.ubicacion = guardado.getUbicacionEvento();
        out.idUsuario = guardado.getIdUsuario().getId();
        out.createdAt = guardado.getFechaAltaEvento();
        out.updatedAt = guardado.getFechaModificacionEvento();
        out.colaboradoresIds = colaboradoresIds; // Incluir los IDs de colaboradores en la respuesta

        return out;
    }
    
    public List<Evento> obtenerBorradoresPorUsuario(String userId){
        // Verificamos que exista el usuario
        if (!usuarioRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return eventoRepository.findByEstadoEventoAndIdUsuario(indie.models.enums.eventoEstado.BORRADOR, userId);
    }
    
    @Override
    public List<Evento> obtenerPublicadosPorUsuario(String userId){
        // Agregamos logs para depuración
        System.out.println("=== BUSCANDO EVENTOS PUBLICADOS PARA USUARIO: " + userId + " ===");
        
        // Verificamos que exista el usuario
        boolean usuarioExiste = usuarioRepository.existsById(userId);
        System.out.println("¿Usuario existe? " + usuarioExiste);
        
        if (!usuarioExiste) {
            System.out.println("ERROR: Usuario no encontrado");
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        // Obtenemos los eventos
        List<Evento> eventos = eventoRepository.findByEstadoEventoAndIdUsuario(indie.models.enums.eventoEstado.PUBLICADO, userId);
        
        // Imprimimos información sobre los eventos encontrados
        System.out.println("Cantidad de eventos encontrados: " + eventos.size());
        for (Evento evento : eventos) {
            System.out.println("- Evento ID: " + evento.getId() + 
                ", Título: " + evento.getTituloEvento() + 
                ", Estado: " + evento.getEstadoEvento() +
                ", Usuario: " + evento.getIdUsuario().getId());
        }
        
        return eventos;
    }
    
    @Transactional
    public void marcarBorradorComoBaja(String titulo, String userId) {
        // Verificamos que exista el usuario
        if (!usuarioRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        Optional<Evento> borrador = eventoRepository.findByTituloAndEstadoAndIdUsuario(
            titulo, 
            indie.models.enums.eventoEstado.BORRADOR, 
            userId
        );
        
        borrador.ifPresent(e -> {
            e.setFechaBajaEvento(LocalDateTime.now());
            eventoRepository.save(e);
        });
    }
    
    public boolean existeEventoConTituloYFecha(String titulo, LocalDateTime fecha) {
        // Solo verificar duplicados con eventos PUBLICADOS, no con borradores
        return eventoRepository.existsByTituloAndFechaAndEstadoEventoAndFechaBajaIsNull(titulo, fecha, indie.models.enums.eventoEstado.PUBLICADO);
    }

    @Override
    public Evento findById(String id) {
        System.out.println("=== BUSCANDO EVENTO POR ID: " + id + " ===");
        
        // Intentamos encontrar el evento
        Optional<Evento> eventoOpt = eventoRepository.findById(id);
        
        // Verificamos si se encontró
        if (!eventoOpt.isPresent()) {
            System.out.println("ERROR: Evento no encontrado con ID: " + id);
            throw new IllegalArgumentException("Evento no encontrado");
        }
        
        System.out.println("Evento encontrado: " + eventoOpt.get().getId() + ", Título: " + eventoOpt.get().getTituloEvento());
        return eventoOpt.get();
    }
    
    /**
     * Agrega un evento al calendario del usuario creador
     * @param evento Evento a agregar al calendario
     */
    @Transactional
    private void agregarEventoAlCalendario(Evento evento) {
        try {
            // Obtener el calendario del usuario creador del evento
            Optional<Calendario> calendarioOpt = calendarioService.findByUsuarioId(evento.getIdUsuario().getId());
            
            if (calendarioOpt.isPresent()) {
                Calendario calendario = calendarioOpt.get();
                
                // Verificar si el evento ya está en el calendario para evitar duplicados
                List<FechaCalendario> eventosExistentes = fechaCalendarioRepository.findEventosByCalendario(calendario.getId());
                boolean eventoYaExiste = eventosExistentes.stream()
                    .anyMatch(fc -> fc.getIdEvento() != null && fc.getIdEvento().getId().equals(evento.getId()));
                
                if (!eventoYaExiste) {
                    // Extraer fecha y hora del evento
                    LocalDateTime fechaHoraEvento = evento.getFechaHoraEvento();
                    
                    if (fechaHoraEvento != null) {
                        // Crear la entrada en FechaCalendario con las fechas y horas del evento
                        FechaCalendario fechaCalendario = FechaCalendario.builder()
                            .idCalendario(calendario)
                            .idEvento(evento)
                            .fechaDesde(fechaHoraEvento.toLocalDate())  // Fecha del evento
                            .fechaHasta(fechaHoraEvento.toLocalDate())  // Mismo día (eventos de un día)
                            .horaDesde(fechaHoraEvento.toLocalTime())   // Hora de inicio del evento
                            .horaHasta(fechaHoraEvento.toLocalTime().plusHours(2))  // Duración de 2 horas por defecto
                            .todoElDia(false)  // Los eventos tienen hora específica
                            .build();
                        
                        fechaCalendarioRepository.save(fechaCalendario);
                        
                        System.out.println("Evento '" + evento.getTituloEvento() + "' (ID: " + evento.getId() + 
                                         ") agregado al calendario de " + evento.getIdUsuario().getUsername() +
                                         " para el " + fechaHoraEvento.toLocalDate() + 
                                         " de " + fechaHoraEvento.toLocalTime() + 
                                         " a " + fechaHoraEvento.toLocalTime().plusHours(2));
                    } else {
                        System.err.println("No se puede agregar evento al calendario: fechaHoraEvento es null para evento " + evento.getId());
                    }
                } else {
                    System.out.println("El evento '" + evento.getTituloEvento() + "' ya existe en el calendario");
                }
            } else {
                System.err.println("No se encontró calendario para el usuario: " + evento.getIdUsuario().getUsername());
            }
        } catch (Exception e) {
            System.err.println("Error al agregar evento al calendario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Guarda las relaciones de colaboración entre el evento y los usuarios colaboradores
     * @param evento Evento al que se asocian los colaboradores
     * @param colaboradoresIds Lista de IDs de usuarios colaboradores
     */
    @Transactional
    private void guardarColaboradores(Evento evento, List<String> colaboradoresIds) {
        // Primero eliminar todas las colaboraciones previas si existieran (para actualización)
        List<Colaboracion> colaboracionesExistentes = evento.getColaboraciones();
        if (colaboracionesExistentes != null && !colaboracionesExistentes.isEmpty()) {
            System.out.println("Eliminando " + colaboracionesExistentes.size() + " colaboraciones previas");
            colaboracionRepository.deleteAll(colaboracionesExistentes);
            evento.setColaboraciones(new ArrayList<>());
        }
        
        // Ahora crear las nuevas colaboraciones
        if (colaboradoresIds != null && !colaboradoresIds.isEmpty()) {
            System.out.println("Procesando " + colaboradoresIds.size() + " colaboradores para el evento: " + evento.getId());
            
            for (String colaboradorId : colaboradoresIds) {
                try {
                    // Validar que el usuario colaborador exista
                    Usuario colaborador = usuarioRepository.findById(colaboradorId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario colaborador no encontrado: " + colaboradorId));
                    
                    // Crear la entidad de colaboración
                    Colaboracion colaboracion = new Colaboracion();
                    colaboracion.setIdEvento(evento);
                    colaboracion.setIdUsuario(colaborador);
                    colaboracion.setFechaAltaColaboracion(new Date());

                    if (evento.getEstadoEvento() == indie.models.enums.eventoEstado.PUBLICADO) {
                        colaboracion.setEstado(EstadoColaboracion.PENDIENTE);
                    } else {
                        colaboracion.setEstado(EstadoColaboracion.BORRADOR);
                    }
                    
                    // Guardar la colaboración
                    Colaboracion colaboracionGuardada = colaboracionRepository.save(colaboracion);
                    System.out.println("Colaboración guardada: " + colaboracionGuardada.getId() + 
                                      " - Usuario: " + colaborador.getUsername() + 
                                      " - Evento: " + evento.getTituloEvento());
                    
                    // Añadir a la lista de colaboraciones del evento
                    evento.getColaboraciones().add(colaboracionGuardada);

                    // Notificar al colaborador invitado
                    try {
                        String tipo = "Invitación a colaborar";
                        String contenido = "Te invitaron a colaborar en \"" + evento.getTituloEvento() + "\" | evt:" + evento.getId();
                        notificacionService.crear(colaborador, tipo, contenido);
                    } catch (Exception ignored) {}
                    
                } catch (Exception e) {
                    System.err.println("Error al procesar colaborador " + colaboradorId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public List<Evento> obtenerProximosEventosPublicados() {
        System.out.println("=== BUSCANDO PRÓXIMOS EVENTOS PUBLICADOS ===");
        
        // Obtener eventos publicados con fecha mayor o igual a la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();
        List<Evento> eventos = eventoRepository.findPublishedWithDateGreaterThanEqual(indie.models.enums.eventoEstado.PUBLICADO, fechaActual);
        
        // Imprimir información sobre los eventos encontrados
        System.out.println("Cantidad de próximos eventos encontrados: " + eventos.size());
        for (Evento evento : eventos) {
            System.out.println("- Evento ID: " + evento.getId() + 
                ", Título: " + evento.getTituloEvento() + 
                ", Estado: " + evento.getEstadoEvento() +
                ", Fecha: " + evento.getFechaHoraEvento());
        }
        
        return eventos;
    }
}
