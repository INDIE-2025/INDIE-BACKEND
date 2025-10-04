package indie.repositories.moduloComentarios;

import indie.dtos.moduloComentarios.admin.AdminComentarioDenunciaDTO;
import indie.models.moduloComentarios.Denuncia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String> {

    @Query("SELECT new indie.dtos.moduloComentarios.admin.AdminComentarioDenunciaDTO(" +
            "denunciado.nombreUsuario, " +
            "c.comentario, " +
            "c.id, " +
            "d.motivoDenuncia, " +
            "d.createdAt, " +
            "denunciante.nombreUsuario) " +
            "FROM Denuncia d " +
            "JOIN d.idComentario c " +
            "JOIN c.idUsuarioComentador denunciado " +
            "JOIN d.idUsuario denunciante " +
            "WHERE c.deletedAt IS NULL " +
            "AND d.deletedAt IS NULL")
    List<AdminComentarioDenunciaDTO> findActiveComentarioDenuncias();

    List<Denuncia> findByIdComentarioIdAndDeletedAtIsNull(String idComentario);
}
