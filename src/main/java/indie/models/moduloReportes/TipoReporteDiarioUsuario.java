package indie.models.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import indie.models.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoReporteDiarioUsuario extends BaseModel {
    
    @NotNull
    private BigDecimal valorMetrica;
    
    @ManyToOne
    @NotNull
    private TipoMetrica tipoMetrica;
    
    // Métodos de utilidad
    public String getNombreMetrica() {
        return tipoMetrica != null ? tipoMetrica.getNombre() : null;
    }
    
    public String getUnidadMedida() {
        return tipoMetrica != null ? tipoMetrica.getUnidadMedida() : null;
    }
}