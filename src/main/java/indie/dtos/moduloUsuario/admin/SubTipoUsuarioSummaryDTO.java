package indie.dtos.moduloUsuario.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubTipoUsuarioSummaryDTO {
    private String nombreSubTipoUsuario;
    private String estado;
}
