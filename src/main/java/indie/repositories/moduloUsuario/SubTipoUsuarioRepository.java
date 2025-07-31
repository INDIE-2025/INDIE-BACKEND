package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.SubTipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTipoUsuarioRepository extends JpaRepository<SubTipoUsuario, String> {
}
