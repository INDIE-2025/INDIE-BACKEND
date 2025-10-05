package indie.repositories;

import indie.models.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import indie.models.enums.TipoClase;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo,String> {
    List<Archivo> findByIdObjetoAndTipoClase(String idObjeto, TipoClase tipoClase);
    List<Archivo> findByIdObjetoAndTipoClaseOrderByOrdenAscCreatedAtAsc(String idObjeto, TipoClase tipoClase);
}
