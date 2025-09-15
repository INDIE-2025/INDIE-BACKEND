package indie.dtos.auth;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.Usuario;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserResponseDTO usuario;

    public static LoginResponse fromUsuarioAndToken(Usuario usuario, String token) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        
        UserResponseDTO usuarioDTO = new UserResponseDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombreUsuario());
        usuarioDTO.setApellido(usuario.getApellidoUsuario());
        usuarioDTO.setEmail(usuario.getEmailUsuario());
        usuarioDTO.setUsername(usuario.getUsername());
        
        SubTipoUsuario subTipo = usuario.getSubTipoUsuario();
        if (subTipo != null) {
            UserSubTipoResponseDTO subTipoDTO = new UserSubTipoResponseDTO();
            subTipoDTO.setId(subTipo.getId());
            subTipoDTO.setNombreSubTipoUsuario(subTipo.getNombreSubTipoUsuario());
            usuarioDTO.setSubTipoUsuario(subTipoDTO);
        }
        
        response.setUsuario(usuarioDTO);
        return response;
    }
}

@Data
class UserResponseDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private UserSubTipoResponseDTO subTipoUsuario;
}

@Data
class UserSubTipoResponseDTO {
    private String id;
    private String nombreSubTipoUsuario;
}