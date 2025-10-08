package indie.dtos.moduloCalendario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloquearFechasDTO {
    
    @NotNull(message = "El ID del calendario es obligatorio")
    private String calendarioId;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
    
    private LocalTime horaInicio;
    
    private LocalTime horaFin;
    
    private String motivo;
    
    private Boolean todoElDia;
}