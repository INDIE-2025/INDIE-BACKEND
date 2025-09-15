package indie.models.moduloComentarios;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Column;
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
@Table(name = "Comentario")
public class ComentarUsuario extends BaseModel {

    @Column(nullable = false, length = 1000)
    private String comentario;

    @ManyToOne
    @JoinColumn(name="idUsuarioComentado",  nullable = false)
    private Usuario idUsuarioComentado;
    @ManyToOne
    @JoinColumn(name = "idUsuarioComentador", nullable = false)
    private Usuario idUsuarioComentador;

}
