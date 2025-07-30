package indie.models.moduloMensajeria;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.prefs.BackingStoreException;

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Mensaje")
public class Mensaje extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "idEmisor")
    private Usuario idEmisor;
    @ManyToOne
    @JoinColumn(name = "idChat")
    private Chat idChat;

    private String mensaje;


}
