package indie.models.moduloComentarios;

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

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Comentario")
public class ComentarUsuario extends BaseModel {

    private String comentario;
    @ManyToOne
    @JoinColumn(name="idUsuarioComentado")
    private Usuario idUsuarioComentado;
    @ManyToOne
    @JoinColumn(name = "idUsuarioComentador")
    private Usuario idUsuarioComentador;



}
