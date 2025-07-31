package indie.services.moduloEventos;

import indie.models.moduloEventos.Interes;
import indie.repositories.moduloEventos.InteresRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteresServiceImpl extends BaseServiceImpl<Interes, String> implements InteresService {

    InteresRepository interesRepository;

    @Autowired
    public InteresServiceImpl(InteresRepository interesRepository) {
        super(interesRepository);
        this.interesRepository = interesRepository;
    }
}
