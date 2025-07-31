package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.FuncionalidadRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionalidadRolRepository extends JpaRepository<FuncionalidadRol, String> {
}
