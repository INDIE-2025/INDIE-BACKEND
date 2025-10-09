package indie.repositories.moduloEventos;

import indie.models.moduloEventos.ArchivoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoEventoRepository extends JpaRepository<ArchivoEvento, String> {
    
    @Query("SELECT a FROM ArchivoEvento a WHERE a.idEvento.id = :eventoId")
    List<ArchivoEvento> findByEventoId(@Param("eventoId") String eventoId);
}
