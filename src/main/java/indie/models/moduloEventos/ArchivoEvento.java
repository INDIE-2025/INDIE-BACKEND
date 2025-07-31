package indie.models.moduloEventos;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ArchivoEvento extends BaseModel {

    @NotNull
    private Date fechaAltaArchivoEvento;

    @NotNull
    private Date fechaBajaArchivoEvento;

    @NotNull
    private String tipoArchivoEvento;

    @NotNull
    private String urlArchivoEvento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idEvento")
    private Evento idEvento;
}
