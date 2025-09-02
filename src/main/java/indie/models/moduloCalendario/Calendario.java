package indie.models.moduloCalendario;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Calendario extends BaseModel {
    private String zonaHoraria;

}
