package indie.services;

import indie.models.Archivo;
import indie.repositories.ArchivoRepository;
import indie.models.enums.TipoClase;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArchivoService extends BaseServiceImpl<Archivo, String> {

    public ArchivoService(ArchivoRepository archivoRepository) {
        super(archivoRepository);
    }

    public List<Archivo> listarPorObjetoYClase(String idObjeto, TipoClase tipoClase) {
        var repo = (ArchivoRepository) repository;
        var ordered = repo.findByIdObjetoAndTipoClaseOrderByOrdenAscCreatedAtAsc(idObjeto, tipoClase);
        if (ordered != null && !ordered.isEmpty()) return ordered;
        return repo.findByIdObjetoAndTipoClase(idObjeto, tipoClase);
    }
}
