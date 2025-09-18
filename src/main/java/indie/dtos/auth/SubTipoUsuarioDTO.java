package indie.dtos.auth;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubTipoUsuarioDTO {
    private String id;
    private String nombreSubTipoUsuario;
    private String nombreTipoUsuario;
    private LocalDateTime deletedAt;
}

