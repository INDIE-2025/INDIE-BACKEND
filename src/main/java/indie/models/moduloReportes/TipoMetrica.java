package indie.models.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import indie.models.BaseModel;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMetrica extends BaseModel {
    
    @NotNull
    private String nombre;
    
    private String descripcion;
    
    @NotNull
    private String unidadMedida;
    
    @NotNull
    private Boolean activo = true;
}
