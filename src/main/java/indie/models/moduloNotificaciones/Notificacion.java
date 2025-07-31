package indie.models.moduloNotificaciones;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
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
public class Notificacion extends BaseModel {

    @NotNull
    private Date fechaAltaNotificacion;

    @NotNull
    private Date fechaBajaNotificacion;

    // Puede ser null si la notificación no ha sido leída
    private Date fechaLecturaNotificacion;

    @NotNull
    private String contenidoNotificacion;
    // Le cambie el nombre a contenidoNotificacion para que sea más descriptivo

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idTipoNotificacion")
    private TipoNotificacion idTipoNotificacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;



}
