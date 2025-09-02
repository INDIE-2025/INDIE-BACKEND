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

@Getter // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Evento extends BaseModel {

    @ManyToOne
    private Usuario idUsuario;

    @NotNull
    private String descripcionEvento;

    @NotNull
    private Date fechaHoraEvento;

    @NotNull
    private String tituloEvento;

    @NotNull
    private String ubicacionEvento;

    @NotNull
    private Date fechaAltaEvento;

    @NotNull
    private Date fechaBajaEvento;


}
