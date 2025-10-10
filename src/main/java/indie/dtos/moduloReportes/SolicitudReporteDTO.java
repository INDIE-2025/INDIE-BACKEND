package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudReporteDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoReporte; // "USUARIO", "SISTEMA", etc.
    private String formato; // "JSON", "PDF", "EXCEL"
    private Boolean incluirGraficos;
}