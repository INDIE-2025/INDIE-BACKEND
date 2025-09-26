package indie.repositories.moduloCalendario;

import indie.models.moduloCalendario.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioRepository extends JpaRepository<Calendario, String> {




}
