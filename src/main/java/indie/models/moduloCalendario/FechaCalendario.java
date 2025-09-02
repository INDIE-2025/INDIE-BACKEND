package indie.models.moduloCalendario;

import indie.models.BaseModel;
import indie.models.moduloEventos.Evento;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "FechaCalendario")
public class FechaCalendario extends BaseModel {

    private Date fechaHoraFCalendario;
    @ManyToOne
    @JoinColumn(name = "idCalendario")
    private Calendario idCalendario;
    @ManyToOne
    @JoinColumn(name = "idEvento")
    private Evento idEvento;

}
