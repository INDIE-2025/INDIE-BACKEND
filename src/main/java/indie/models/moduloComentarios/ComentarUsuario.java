package indie.models.moduloComentarios;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
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
public class ComentarUsuario extends BaseModel {

    private String comentario;
    private Usuario idUsuarioComentado;
    private Usuario idUsuarioComentador;



}
