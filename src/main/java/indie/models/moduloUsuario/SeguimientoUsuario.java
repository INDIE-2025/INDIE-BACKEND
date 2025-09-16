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

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SeguimientoUsuario extends BaseModel {

    @NotNull
    private boolean bloqueado = false;
    
    @ManyToOne
    @NotNull
    private Usuario usuarioSeguido;
    @ManyToOne
    @NotNull
    private Usuario usuarioSeguidor;

}
