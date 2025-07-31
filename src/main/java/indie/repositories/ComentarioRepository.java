package indie.repositories;

import indie.models.moduloComentarios.ComentarUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarUsuario, String> {


    @Query(value = "SELECT * FROM Comentario c WHERE c.id_usuario_comentado = :idUsuarioComentado",
    nativeQuery = true)
    List<ComentarUsuario> traerComentariosDeUnUsuario(@Param("idUsuarioComentado") String idUsuarioComentado);


}
