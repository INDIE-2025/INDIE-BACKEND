package indie.models.moduloNotificaciones;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class TipoNotificacionUsuario extends BaseModel {

    @NotNull
    private Date fechaAltaTipoNotificacion;

    @NotNull
    private Date fechaBajaTipoNotificacion;

    @NotNull
    private boolean notificacionUsuarioActiva;
    // Le cambie el nombre a notificacionUsuarioActiva para que sea mas descriptivo
    // Marca si un usuario tiene activa una notificación de un tipo específico

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idTipoNotificacion")
    private TipoNotificacion idTipoNotificacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;
}
