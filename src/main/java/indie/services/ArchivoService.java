package indie.services;

import indie.models.Archivo;
import indie.repositories.ArchivoRepository;
import org.springframework.stereotype.Service;

@Service
public class ArchivoService extends BaseServiceImpl<Archivo, String> {

    public ArchivoService(ArchivoRepository archivoRepository) {
        super(archivoRepository);
    }

}
