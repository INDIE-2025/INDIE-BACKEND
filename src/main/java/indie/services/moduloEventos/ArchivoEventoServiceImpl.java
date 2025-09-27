package indie.services.moduloEventos;

import indie.models.moduloEventos.ArchivoEvento;
import indie.repositories.moduloEventos.ArchivoEventoRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArchivoEventoServiceImpl extends BaseServiceImpl<ArchivoEvento, String> implements ArchivoEventoService {

    ArchivoEventoRepository archivoEventoRepository;

    public ArchivoEventoServiceImpl(ArchivoEventoRepository archivoEventoRepository) {
        super(archivoEventoRepository);
        this.archivoEventoRepository = archivoEventoRepository;
    }
}
