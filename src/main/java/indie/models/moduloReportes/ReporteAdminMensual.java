package indie.models.moduloReportes;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
@Entity
public class ReporteAdminMensual extends BaseModel {

    @NotNull
    private Integer anio;

    @NotNull
    private Integer mes; // 1-12

    @ManyToOne
    @JoinColumn(name = "idTipoMetrica")
    private TipoMetrica tipoMetrica;

    @NotNull
    private Long valor;
}

