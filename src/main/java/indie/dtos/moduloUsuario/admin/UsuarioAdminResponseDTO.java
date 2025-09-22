package indie.dtos.moduloUsuario.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioAdminResponseDTO {
    private String nombre;
    private String nombreUsuario;
    private String email;
    private String tipoUsuario;
    private String estado;
}
