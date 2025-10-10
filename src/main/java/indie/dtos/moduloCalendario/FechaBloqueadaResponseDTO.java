package indie.dtos.moduloCalendario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FechaBloqueadaResponseDTO {
    
    private String id;
    private String calendarioId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
    private Boolean todoElDia;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private boolean activo;
}