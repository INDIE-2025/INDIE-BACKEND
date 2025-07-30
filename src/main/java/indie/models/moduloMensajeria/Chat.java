package indie.models.moduloMensajeria;

import indie.models.BaseModel;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Chat extends BaseModel {

    private String atributo; //atributo fake para que se creen las tablas porque sino me tira error

}
