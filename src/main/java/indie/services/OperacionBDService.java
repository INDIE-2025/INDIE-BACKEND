package indie.services;

import indie.models.moduloBackUp.OperacionBD;
import indie.repositories.OperacionBDRepository;
import org.springframework.stereotype.Service;

@Service
public class OperacionBDService extends BaseServiceImpl<OperacionBD, String> {

    OperacionBDRepository operacionBDRepository;

    public OperacionBDService(OperacionBDRepository operacionBDRepository) {
        super(operacionBDRepository);
        this.operacionBDRepository = operacionBDRepository;
    }
}
