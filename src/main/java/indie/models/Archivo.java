package indie.models;

import indie.models.enums.TipoClase;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Archivo extends BaseModel{

    @NotNull
    private String nombreArchivo;
    @NotNull
    private TipoClase tipoClase;
    @NotNull
    private String tipoArchivo;
    @NotNull
    private String urlArchivo;
    @NotNull
    private String idObjeto;

}
