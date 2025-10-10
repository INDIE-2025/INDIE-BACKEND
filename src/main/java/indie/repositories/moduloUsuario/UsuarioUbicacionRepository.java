package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.UsuarioUbicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioUbicacionRepository extends JpaRepository<UsuarioUbicacion, String> {
    List<UsuarioUbicacion> findByUsuario_Id(String usuarioId);
}

