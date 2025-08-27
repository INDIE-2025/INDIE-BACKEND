package indie.dtos.moduloBackup;

import indie.models.enums.EstadoOperacion;
import indie.models.enums.TipoOperacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperacionBDCreateDto {
    
    @NotBlank
    private String nombreOperacion;

    @NotNull
    private TipoOperacion tipo;

    @NotNull
    private EstadoOperacion estado;

    @NotBlank
    private String usuarioEmail;    

}
