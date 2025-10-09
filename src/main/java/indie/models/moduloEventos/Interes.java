package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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

    private Date fechaBajaInteres;
}
