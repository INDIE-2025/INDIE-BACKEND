package indie.repositories;

import indie.models.moduloComentarios.ComentarUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarUsuario, Long> {




}
