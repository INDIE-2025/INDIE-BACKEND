package indie.models.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import indie.models.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteAdminMensual extends BaseModel {
    
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
