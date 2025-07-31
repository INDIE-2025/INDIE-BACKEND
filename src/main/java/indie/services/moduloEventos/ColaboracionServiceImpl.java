package indie.services.moduloEventos;

import indie.models.moduloEventos.Colaboracion;
import indie.repositories.moduloEventos.ColaboracionRepository;
import indie.services.BaseService;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColaboracionServiceImpl extends BaseServiceImpl<Colaboracion, String> implements ColaboracionService {

    ColaboracionRepository colaboracionRepository;

    @Autowired
    public ColaboracionServiceImpl(ColaboracionRepository colaboracionRepository) {
        super(colaboracionRepository);
        this.colaboracionRepository = colaboracionRepository;
    }

}
