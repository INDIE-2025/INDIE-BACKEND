package indie.models.moduloRecomendaciones;

import indie.models.BaseModel;
import indie.models.moduloEventos.Evento;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RecomendacionEventoDiaria extends BaseModel {

    @NotNull
    private int cantidadInteresados;

    @ManyToOne
    @NotNull
    private Evento evento;

}
