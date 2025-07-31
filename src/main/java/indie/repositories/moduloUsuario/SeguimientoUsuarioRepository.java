package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.SeguimientoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeguimientoUsuarioRepository extends JpaRepository<SeguimientoUsuario, String> {
}
