package indie.models.moduloNotificaciones;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
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
public class TipoNotificacion extends BaseModel {

    @NotNull
    private String nombreTipoNotificacion;

    @NotNull
    private boolean notificarEmailTipoNotificacion;
    // Marca si el admin permite notificar por email

    @NotNull
    private Date fechaAltaTipoNotificacion;

    @NotNull
    private Date fechaBajaTipoNotificacion;

}
