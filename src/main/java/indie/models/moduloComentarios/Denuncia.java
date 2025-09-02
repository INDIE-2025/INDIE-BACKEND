package indie.models.moduloComentarios;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter// incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Denuncia")
public class Denuncia extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "idComentario")
    private ComentarUsuario idComentario;
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;
    private String motivoDenuncia;

}
