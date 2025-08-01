package indie.services.moduloBackup;

import indie.models.moduloBackUp.OperacionBD;
import indie.repositories.moduloBackup.OperacionBDRepository;
import indie.services.BaseServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class OperacionBDService extends BaseServiceImpl<OperacionBD, String> {

    OperacionBDRepository operacionBDRepository;

    public OperacionBDService(OperacionBDRepository operacionBDRepository) {
        super(operacionBDRepository);
        this.operacionBDRepository = operacionBDRepository;
    }
}
