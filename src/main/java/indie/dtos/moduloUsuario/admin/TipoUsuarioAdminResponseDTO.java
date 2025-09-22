package indie.dtos.moduloUsuario.admin;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TipoUsuarioAdminResponseDTO {
    private String nombreTipoUsuario;
    private List<String> subtipos;
    private long cantidadUsuarios;
    private String fechaAlta;
    private String estado;
}

