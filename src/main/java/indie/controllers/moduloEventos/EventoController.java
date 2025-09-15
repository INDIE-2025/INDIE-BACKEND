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
        return ResponseEntity.ok(r);
    }

}
