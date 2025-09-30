package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ConfiguracionSistema extends BaseModel{

    @NotNull
    private String nombreConfiguracion;

    @NotNull
    private boolean activo;
    private int valor;
}
