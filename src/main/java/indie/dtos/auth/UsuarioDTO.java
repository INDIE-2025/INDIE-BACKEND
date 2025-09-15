package indie.dtos.auth;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private SubTipoUsuarioDTO subTipoUsuario;
}