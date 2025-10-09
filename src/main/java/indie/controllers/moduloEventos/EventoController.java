  package indie.controllers.moduloEventos;

import indie.controllers.BaseController;
import indie.models.moduloEventos.Evento;
import indie.dtos.moduloEventos.EventoResponse;
import indie.dtos.moduloEventos.crearEventoDTO;
import indie.services.moduloEventos.EventoService;
import jakarta.validation.Valid;
import java.util.List;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/eventos")
public class EventoController extends BaseController<Evento, String> {

    protected EventoService eventoService;

    public EventoController(EventoService eventoService) {
        super(eventoService);
        this.eventoService = eventoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<EventoResponse> crear(@Valid @RequestBody crearEventoDTO body){
        EventoResponse creado = eventoService.crear(body); // hace el save(...) en el repo
        return ResponseEntity.created(URI.create("/api/eventos/" + creado.id)).body(creado);
    }
    
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EventoResponse> actualizar(@PathVariable("id") String id, @Valid @RequestBody crearEventoDTO body){
        EventoResponse actualizado = eventoService.actualizar(id, body);
        return ResponseEntity.ok(actualizado);
    }
    
    @PostMapping("/borrador")
    public ResponseEntity<EventoResponse> guardarBorrador(@Valid @RequestBody crearEventoDTO body){
        EventoResponse borrador = eventoService.guardarBorrador(body);
        return ResponseEntity.created(URI.create("/api/eventos/" + borrador.id)).body(borrador);
    }

    @GetMapping("/borradores")
    public ResponseEntity<List<EventoResponse>> obtenerBorradoresPorUsuario(@RequestParam ("usuario") String usuario) {
        List<Evento> borradores = eventoService.obtenerBorradoresPorUsuario(usuario);
        List<EventoResponse> response = borradores.stream().map(e -> {
            EventoResponse r = new EventoResponse();
            r.id = e.getId();
            r.titulo = e.getTituloEvento();
            r.descripcion = e.getDescripcionEvento();
            r.fechaHoraEvento = e.getFechaHoraEvento();
            r.ubicacion = e.getUbicacionEvento();
            r.idUsuario = e.getIdUsuario().getId();
            r.createdAt = e.getFechaAltaEvento();
            r.updatedAt = e.getFechaModificacionEvento();
            
            // Obtener los IDs de colaboradores
            r.colaboradoresIds = e.getColaboraciones().stream()
                .map(c -> c.getIdUsuario().getId())
                .toList();
                
            return r;
        }).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/publicados")
    public ResponseEntity<List<EventoResponse>> obtenerPublicadosPorUsuario(@RequestParam ("usuario") String usuario) {
        List<Evento> publicados = eventoService.obtenerPublicadosPorUsuario(usuario);
        List<EventoResponse> response = publicados.stream().map(e -> {
            EventoResponse r = new EventoResponse();
            r.id = e.getId();
            r.titulo = e.getTituloEvento();
            r.descripcion = e.getDescripcionEvento();
            r.fechaHoraEvento = e.getFechaHoraEvento();
            r.ubicacion = e.getUbicacionEvento();
            r.idUsuario = e.getIdUsuario().getId();
            r.createdAt = e.getFechaAltaEvento();
            r.updatedAt = e.getFechaModificacionEvento();
            
            // Obtener los IDs de colaboradores
            r.colaboradoresIds = e.getColaboraciones().stream()
                .map(c -> c.getIdUsuario().getId())
                .toList();
                
            return r;
        }).toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getById(@PathVariable("id") String id) {
        return super.getById(id);
    }
    
    @GetMapping("/detalle/{id}")
    public ResponseEntity<EventoResponse> getEvento(@PathVariable("id") String id) {
        Evento evento = eventoService.findById(id);
        EventoResponse r = new EventoResponse();
        r.id = evento.getId();
        r.titulo = evento.getTituloEvento();
        r.descripcion = evento.getDescripcionEvento();
        r.fechaHoraEvento = evento.getFechaHoraEvento();
        r.ubicacion = evento.getUbicacionEvento();
        r.idUsuario = evento.getIdUsuario().getId();
        
        // Obtener los IDs de colaboradores
        r.colaboradoresIds = evento.getColaboraciones().stream()
            .map(c -> c.getIdUsuario().getId())
            .toList();
            
        return ResponseEntity.ok(r);
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<EventoResponse>> obtenerEventosPorIdUsuario(@PathVariable("idUsuario") String idUsuario) {
        System.out.println("=== CONTROLLER: Recibida petición para eventos de usuario: " + idUsuario + " ===");
        
        // Obtener todos los eventos publicados creados por el usuario específico
        List<Evento> eventos = eventoService.obtenerPublicadosPorUsuario(idUsuario);
        System.out.println("Eventos obtenidos correctamente: " + eventos.size());
        
        List<EventoResponse> response = eventos.stream().map(e -> {
            EventoResponse r = new EventoResponse();
            r.id = e.getId();
            r.titulo = e.getTituloEvento();
            r.descripcion = e.getDescripcionEvento();
            r.fechaHoraEvento = e.getFechaHoraEvento();
            r.ubicacion = e.getUbicacionEvento();
            r.idUsuario = e.getIdUsuario().getId();
            r.createdAt = e.getFechaAltaEvento();
            r.updatedAt = e.getFechaModificacionEvento();
            
            // Obtener los IDs de colaboradores
            r.colaboradoresIds = e.getColaboraciones().stream()
                .map(c -> c.getIdUsuario().getId())
                .toList();
                
            return r;
        }).toList();
        
        return ResponseEntity.ok(response);
    }
}
