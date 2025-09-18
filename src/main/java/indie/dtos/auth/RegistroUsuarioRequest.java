package indie.dtos.auth;

import lombok.Data;

@Data
public class RegistroUsuarioRequest {
    private String nombreUsuario;
    private String apellidoUsuario;
    private String emailUsuario;
    private String username;
    private String password;
    private String descripcionUsuario;
    private String youtubeUsuario;
    private String spotifyUsuario;
    private String instagramUsuario;
    private String subTipoUsuarioId;
}

