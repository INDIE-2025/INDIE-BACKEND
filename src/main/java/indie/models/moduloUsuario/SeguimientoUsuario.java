package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SeguimientoUsuario extends BaseModel {

    @NotNull
    private boolean bloqueado = false;

    @NotNull
    private LocalDateTime fechaDesdeSeguimientoUsuario;
    private LocalDateTime fechaHastaSeguimientoUsuario;

    @ManyToOne
    @NotNull
    private Usuario usuarioSeguido;
    @ManyToOne
    @NotNull
    private Usuario usuarioSeguidor;

}
