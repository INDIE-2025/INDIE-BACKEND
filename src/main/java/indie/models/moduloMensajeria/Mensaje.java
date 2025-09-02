package indie.models.moduloMensajeria;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
