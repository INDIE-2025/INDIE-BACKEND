package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteExportacionDTO {
    private LocalDate fecha;
    private String nombreMetrica;
    private BigDecimal valorMetrica;
    private String unidadMedida;
    private String descripcionMetrica;
}