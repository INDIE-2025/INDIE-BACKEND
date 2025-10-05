package indie.models;

import indie.models.enums.TipoClase;
import jakarta.persistence.Entity;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // Datos binarios opcionales (para almacenar contenido en BD)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] contenido;

    private String contentType;

    // Metadatos opcionales
    private String titulo;
    private String descripcion;

    // Orden de presentacion (menor primero)
    private Integer orden;

}
