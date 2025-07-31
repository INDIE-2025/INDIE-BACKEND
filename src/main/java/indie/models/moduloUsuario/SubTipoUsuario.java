package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SubTipoUsuario extends BaseModel {

    @NotNull
    private String nombreSubTipoUsuario;

    @ManyToOne
    @NotNull
    private TipoUsuario tipoUsuario;

}
