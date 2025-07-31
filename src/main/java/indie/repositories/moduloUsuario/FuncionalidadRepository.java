package indie.repositories.moduloUsuario;

import indie.models.moduloUsuario.Funcionalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionalidadRepository extends JpaRepository<Funcionalidad,String> {
}
