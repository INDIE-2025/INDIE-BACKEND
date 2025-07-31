package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.enums.EstadoColaboración;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.*;
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
public class Colaboracion extends BaseModel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idEvento")
    private Evento idEvento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;

    @NotNull
    private Date fechaAltaColaboracion;

    @NotNull
    private Date fechaBajaColaboracion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoColaboración estado;


}
