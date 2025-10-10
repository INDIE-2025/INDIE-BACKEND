package indie.repositories.moduloEventos;

import indie.models.moduloEventos.ArchivoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivoEventoRepository extends JpaRepository<ArchivoEvento, String> {
}
