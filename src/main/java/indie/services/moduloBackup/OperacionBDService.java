package indie.services.moduloBackup;

import indie.dtos.moduloBackup.OperacionBDDTO;
import indie.models.moduloBackUp.OperacionBD;
import indie.services.BaseService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface OperacionBDService extends BaseService<OperacionBD, String> {

    List<OperacionBDDTO> obtenerOperacionesDTO();
}
