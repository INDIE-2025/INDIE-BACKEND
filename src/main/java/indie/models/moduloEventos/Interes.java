package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
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
public class Interes extends BaseModel {

    @NotNull
    @ManyToOne
    private Evento idEvento;

    @NotNull
    @ManyToOne
    private Usuario idUsuario;

    @NotNull
    private Date fechaAltaInteres;

    @NotNull
    private Date fechaBajaInteres;
}
