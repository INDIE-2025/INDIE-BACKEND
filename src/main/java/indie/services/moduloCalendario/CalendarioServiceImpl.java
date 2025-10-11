package indie.services.moduloCalendario;

import indie.dtos.moduloCalendario.CalendarioDTO;
import indie.models.moduloCalendario.Calendario;
import indie.models.moduloCalendario.FechaCalendario;
import indie.repositories.moduloCalendario.CalendarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarioServiceImpl extends BaseServiceImpl<Calendario,String> implements CalendarioService{
    CalendarioRepository calendarioRepository;

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

    /**
     * Convertir Calendario a DTO evitando referencias circulares
     */
    public CalendarioDTO convertirADTO(Calendario calendario) {
        if (calendario == null) {
            return null;
        }

        List<CalendarioDTO.FechaCalendarioDTO> fechasDTO = new ArrayList<>();

        if (calendario.getFechasCalendario() != null) {
            fechasDTO = calendario.getFechasCalendario().stream()
                .filter(fecha -> fecha.getDeletedAt() == null) // Solo fechas activas
                .map(this::convertirFechaADTO)
                .collect(Collectors.toList());
        }

        return CalendarioDTO.builder()
            .id(calendario.getId())
            .zonaHoraria(calendario.getZonaHoraria())
            .fechas(fechasDTO)
            .build();
    }

    /**
     * Convertir FechaCalendario a DTO
     */
    private CalendarioDTO.FechaCalendarioDTO convertirFechaADTO(FechaCalendario fecha) {
        CalendarioDTO.FechaCalendarioDTO.FechaCalendarioDTOBuilder builder = CalendarioDTO.FechaCalendarioDTO.builder()
            .id(fecha.getId())
            .fechaDesde(fecha.getFechaDesde())
            .fechaHasta(fecha.getFechaHasta())
            .horaDesde(fecha.getHoraDesde())
            .horaHasta(fecha.getHoraHasta())
            .todoElDia(fecha.getTodoElDia())
            .motivo(fecha.getMotivo());

        if (fecha.esEvento()) {
            // Es un evento
            builder
                .tipo("EVENTO")
                .eventoId(fecha.getEventoId())
                .eventoTitulo(fecha.getEventoTitulo())
                .eventoFechaHora(fecha.getEventoFechaHora());
                
            // Información adicional del evento si es necesario
            if (fecha.getIdEvento() != null) {
                builder
                    .eventoDescripcion(fecha.getIdEvento().getDescripcionEvento())
                    .eventoUbicacion(fecha.getIdEvento().getUbicacionEvento())
                    .eventoUsuarioId(fecha.getIdEvento().getUsuarioId())
                    .eventoUsuarioNombre(fecha.getIdEvento().getUsuarioNombre())
                    .eventoUsername(fecha.getIdEvento().getUsername());
            }
        } else {
            // Es un bloqueo
            builder.tipo("BLOQUEO");
        }

        return builder.build();
    }

    /**
     * Obtener calendario como DTO por usuario ID
     */
    public Optional<CalendarioDTO> findDTOByUsuarioId(String usuarioId) {
        return findByUsuarioId(usuarioId).map(this::convertirADTO);
    }

    /**
     * Obtener calendario como DTO por username
     */
    public Optional<CalendarioDTO> findDTOByUsername(String username) {
        return findByUsername(username).map(this::convertirADTO);
    }
}
