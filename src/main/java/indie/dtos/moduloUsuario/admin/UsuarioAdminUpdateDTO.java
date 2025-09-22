package indie.dtos.moduloUsuario.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioAdminUpdateDTO {

    @NotBlank
    private String usuario;

    @NotBlank
    private String nombre;

    @NotBlank
    @Email
    private String email;

    @JsonProperty("tipoUsuario")
    @NotBlank
    private String tipoUsuarioNombre;

    @JsonProperty("subtipoUsuario")
    private String subTipoUsuarioNombre;

    @NotBlank
    private String estado;
}
