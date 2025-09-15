package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.enums.EstadoColaboracion;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.*;
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
    private EstadoColaboracion estado;


}
