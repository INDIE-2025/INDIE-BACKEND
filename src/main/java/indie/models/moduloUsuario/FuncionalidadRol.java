package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
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
public class FuncionalidadRol extends BaseModel {

    @ManyToOne
    @NotNull
    private Funcionalidad funcionalidad;

    @ManyToOne
    @NotNull
    private TipoUsuario tipoUsuario;

}
