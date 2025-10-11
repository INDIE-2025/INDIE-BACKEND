package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricaDTO {
    private String nombreMetrica;
    private BigDecimal valor;
    private String unidadMedida;
    private LocalDateTime fechaMetrica;
    private LocalDate fecha; // Para agrupación por día
    private Integer mes; // Para agrupación por mes
    private Integer anio;
    private String metadatos;
}