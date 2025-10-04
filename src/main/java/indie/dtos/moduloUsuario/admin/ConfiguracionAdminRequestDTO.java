package indie.dtos.moduloUsuario.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionAdminRequestDTO {

    @Valid
    @NotNull
    private ConfiguracionWrapper configuraciones;

    @Valid
    @NotNull
    private PermisosWrapper permisos;

    @Getter
    @Setter
    public static class ConfiguracionWrapper {

        @Valid
        @NotEmpty
        private List<ItemConfiguracionDTO> items;
    }

    @Getter
    @Setter
    public static class ItemConfiguracionDTO {

        @NotBlank
        private String nombre;

        @NotNull
        private Boolean activo;

        private Integer valor;
    }

    @Getter
    @Setter
    public static class PermisosWrapper {

        @Valid
        @NotEmpty
        private List<PermisoPorUsuarioDTO> usuarios;
    }

    @Getter
    @Setter
    public static class PermisoPorUsuarioDTO {

        @NotBlank
        private String tipoUsuario;

        @Valid
        @NotEmpty
        private List<PermisoEstadoDTO> permisos;
    }

    @Getter
    @Setter
    public static class PermisoEstadoDTO {

        @NotBlank
        private String nombre;

        @NotNull
        private Boolean activo;
    }
}
