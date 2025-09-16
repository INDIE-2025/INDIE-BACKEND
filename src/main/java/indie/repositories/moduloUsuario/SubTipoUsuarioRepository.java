package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.SubTipoUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTipoUsuarioRepository extends JpaRepository<SubTipoUsuario, String> {

    List<SubTipoUsuario> findByTipoUsuario_Id(String tipoUsuarioId);

    List<SubTipoUsuario> findByTipoUsuario_IdAndDeletedAtIsNull(String tipoUsuarioId);

    Optional<SubTipoUsuario> findByTipoUsuario_IdAndNombreSubTipoUsuarioIgnoreCase(String tipoUsuarioId, String nombreSubTipoUsuario);

    Optional<SubTipoUsuario> findByNombreSubTipoUsuarioIgnoreCase(String nombreSubTipoUsuario);
}
