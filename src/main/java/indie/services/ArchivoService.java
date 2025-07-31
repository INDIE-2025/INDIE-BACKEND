package indie.services;

import indie.models.Archivo;
import indie.repositories.ArchivoRepository;
import org.springframework.stereotype.Service;

@Service
public class ArchivoService extends BaseServiceImpl<Archivo, String> {

    private final ArchivoRepository archivoRepository;

    public ArchivoService(ArchivoRepository archivoRepository) {
        super(archivoRepository);
        this.archivoRepository = archivoRepository;
    }

}
