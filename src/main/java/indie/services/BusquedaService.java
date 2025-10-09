package indie.services;

import indie.dtos.busqueda.EventoBusquedaDTO;
import indie.dtos.busqueda.ResultadoBusquedaDTO;
import indie.dtos.busqueda.UsuarioBusquedaDTO;
import indie.models.moduloEventos.Evento;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloEventos.EventoRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusquedaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    /**
     * Búsqueda unificada que retorna usuarios y eventos
     */
    public ResultadoBusquedaDTO busquedaUnificada(String termino, String tipo, int limite) {
        return busquedaUnificada(termino, tipo, limite, "relevancia");
    }

    /**
     * Búsqueda unificada que retorna usuarios y eventos
     */
    public ResultadoBusquedaDTO busquedaUnificada(String termino, String tipo, int limite, String ordenarPor) {
        List<UsuarioBusquedaDTO> usuarios = new ArrayList<>();
        List<EventoBusquedaDTO> eventos = new ArrayList<>();

        if ("all".equals(tipo) || "usuarios".equals(tipo)) {
            usuarios = buscarUsuariosPorTermino(termino, limite);
        }

        if ("all".equals(tipo) || "eventos".equals(tipo)) {
            eventos = buscarEventosPorTermino(termino, limite);
        }

        return ResultadoBusquedaDTO.builder()
                .usuarios(usuarios)
                .eventos(eventos)
                .totalUsuarios(usuarios.size())
                .totalEventos(eventos.size())
                .terminoBuscado(termino)
                .build();
    }

    /**
     * Búsqueda solo de usuarios
     */
    public ResultadoBusquedaDTO buscarUsuarios(String termino, int limite) {
        List<UsuarioBusquedaDTO> usuarios = buscarUsuariosPorTermino(termino, limite);

        return ResultadoBusquedaDTO.builder()
                .usuarios(usuarios)
                .eventos(new ArrayList<>())
                .totalUsuarios(usuarios.size())
                .totalEventos(0)
                .terminoBuscado(termino)
                .build();
    }

    /**
     * Búsqueda solo de eventos
     */
    public ResultadoBusquedaDTO buscarEventos(String termino, int limite) {
        List<EventoBusquedaDTO> eventos = buscarEventosPorTermino(termino, limite);

        return ResultadoBusquedaDTO.builder()
                .usuarios(new ArrayList<>())
                .eventos(eventos)
                .totalUsuarios(0)
                .totalEventos(eventos.size())
                .terminoBuscado(termino)
                .build();
    }

    /**
     * Buscar usuarios por username o nombre
     */
    private List<UsuarioBusquedaDTO> buscarUsuariosPorTermino(String termino, int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        List<Usuario> usuarios = usuarioRepository.findByUsernameContainingIgnoreCaseOrNombreUsuarioContainingIgnoreCaseOrApellidoUsuarioContainingIgnoreCase(
                termino, termino, termino, pageable);

        return usuarios.stream()
                .map(this::convertirAUsuarioBusquedaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar eventos por título o descripción
     */
    private List<EventoBusquedaDTO> buscarEventosPorTermino(String termino, int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        List<Evento> eventos = eventoRepository.findByTituloEventoContainingIgnoreCaseOrDescripcionEventoContainingIgnoreCase(
                termino, termino, pageable);

        return eventos.stream()
                .map(this::convertirAEventoBusquedaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir Usuario a UsuarioBusquedaDTO
     */
    private UsuarioBusquedaDTO convertirAUsuarioBusquedaDTO(Usuario usuario) {
        return UsuarioBusquedaDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidoUsuario(usuario.getApellidoUsuario())
                .nombreCompleto(usuario.getNombreUsuario() + " " + usuario.getApellidoUsuario())
                .emailUsuario(usuario.getEmailUsuario())
                .descripcionUsuario(usuario.getDescripcionUsuario())
                .build();
    }

    /**
     * Convertir Evento a EventoBusquedaDTO
     */
    private EventoBusquedaDTO convertirAEventoBusquedaDTO(Evento evento) {
        return EventoBusquedaDTO.builder()
                .id(evento.getId())
                .tituloEvento(evento.getTituloEvento())
                .descripcionEvento(evento.getDescripcionEvento())
                .fechaHoraEvento(evento.getFechaHoraEvento())
                .ubicacionEvento(evento.getUbicacionEvento())
                .nombreOrganizador(evento.getIdUsuario().getNombreUsuario() + " " + evento.getIdUsuario().getApellidoUsuario())
                .usernameOrganizador(evento.getIdUsuario().getUsername())
                .build();
    }
}