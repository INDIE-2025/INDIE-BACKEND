package indie.dtos.moduloCalendario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarioDTO {
    
    private String id;
    private String zonaHoraria;
    private List<FechaCalendarioDTO> fechas;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FechaCalendarioDTO {
        private String id;
        private LocalDate fechaDesde;
        private LocalDate fechaHasta;
        private LocalTime horaDesde;
        private LocalTime horaHasta;
        private Boolean todoElDia;
        private String motivo;
        
        // Información del evento (si aplica)
        private String eventoId;
        private String eventoTitulo;
        private String eventoDescripcion;
        private LocalDateTime eventoFechaHora;
        private String eventoUbicacion;
        private String eventoUsuarioId;
        private String eventoUsuarioNombre;
        private String eventoUsername;
        
        // Tipo de entrada
        private String tipo; // "EVENTO" o "BLOQUEO"
    }
}