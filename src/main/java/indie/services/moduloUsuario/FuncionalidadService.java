package indie.services.moduloUsuario;

import indie.models.moduloUsuario.Funcionalidad;
import indie.repositories.moduloUsuario.FuncionalidadRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FuncionalidadService extends BaseServiceImpl <Funcionalidad, String> {

    FuncionalidadRepository funcionalidadRepository;

    public FuncionalidadService(FuncionalidadRepository funcionalidadRepository) {
        super(funcionalidadRepository);
        this.funcionalidadRepository = funcionalidadRepository;
    }
}
