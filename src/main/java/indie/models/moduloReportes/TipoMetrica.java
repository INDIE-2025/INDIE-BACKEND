package indie.models.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import indie.models.BaseModel;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoMetrica extends BaseModel {
    
    @NotNull
    private String nombre;
    
    private String descripcion;
    
    @NotNull
    private String unidadMedida;
    
    @NotNull
    @Builder.Default
    private Boolean activo = true;
}
