package indie.services.moduloBackup;

import indie.dtos.moduloBackup.OperacionBDCreateDto;
import indie.models.moduloBackUp.OperacionBD;
import indie.services.BaseService;

import org.springframework.stereotype.Service;

@Service
public interface OperacionBDService extends BaseService<OperacionBD, String> {

    OperacionBD save(OperacionBDCreateDto dto);
}
