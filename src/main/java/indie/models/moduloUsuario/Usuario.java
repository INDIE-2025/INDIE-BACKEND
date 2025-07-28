package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario extends BaseModel {

    @NotNull
    private String nombre;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String rol; // ADMIN, USER, etc.

}
