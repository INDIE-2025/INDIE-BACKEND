package indie.dtos.auth;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.Usuario;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private UsuarioDTO usuario;

    public static AuthResponseDTO fromUsuarioAndToken(Usuario usuario, String token) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombreUsuario());
        usuarioDTO.setApellido(usuario.getApellidoUsuario());
        usuarioDTO.setEmail(usuario.getEmailUsuario());
        usuarioDTO.setUsername(usuario.getUsername());
        
        SubTipoUsuario subTipo = usuario.getSubTipoUsuario();
        if (subTipo != null) {
            SubTipoUsuarioDTO subTipoDTO = new SubTipoUsuarioDTO();
            subTipoDTO.setId(subTipo.getId());
            subTipoDTO.setNombreSubTipoUsuario(subTipo.getNombreSubTipoUsuario());
            usuarioDTO.setSubTipoUsuario(subTipoDTO);
        }
        
        response.setUsuario(usuarioDTO);
        return response;
    }
}



