package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
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
public class Funcionalidad extends BaseModel {

    @NotNull
    private String nombreFuncionalidad;
    @NotNull
    private String descripcionFuncionalidad;

}
