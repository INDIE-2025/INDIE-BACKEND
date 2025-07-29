package indie.models.moduloCalendario;

import indie.models.BaseModel;
import indie.models.moduloEventos.Evento;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FechaCalendario extends BaseModel {

    private Date fechaHoraFCalendario;
    private Calendario idCalendario;
    private Evento idEvento;

}
