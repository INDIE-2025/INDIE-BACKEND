package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenMetricasDTO {
    private BigDecimal totalVisualizaciones;
    private BigDecimal totalUsuariosInteresados;
    private BigDecimal totalSeguidoresNuevos;
    private Integer totalEventos;
    private BigDecimal promedioVisualizacionesDiarias;
    private BigDecimal promedioInteresadosPorEvento;
}