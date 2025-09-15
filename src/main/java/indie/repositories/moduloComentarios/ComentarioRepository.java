package indie.repositories.moduloComentarios;

import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarUsuario, String> {

    @Query("""
      SELECT new indie.dtos.moduloComentarios.ComentarioDTO(
        c.comentario,
        c.idUsuarioComentador.username, 
        c.createdAt,
        c.idUsuarioComentador.id
      )
      FROM ComentarUsuario c
      WHERE c.idUsuarioComentado.id = :idUsuarioComentado
        AND c.deletedAt IS NULL
      ORDER BY c.createdAt DESC
    """)
    List<ComentarioDTO> traerComentariosDeUnUsuario(@Param("idUsuarioComentado") String idUsuarioComentado);

}

