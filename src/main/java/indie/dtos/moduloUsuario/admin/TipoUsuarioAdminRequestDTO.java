package indie.dtos.moduloUsuario.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoUsuarioAdminRequestDTO {

    @NotBlank
    private String nombreTipoUsuario;

    @NotEmpty
    private List<@NotBlank String> subtipos;

    @NotBlank
    private String estado;
}
