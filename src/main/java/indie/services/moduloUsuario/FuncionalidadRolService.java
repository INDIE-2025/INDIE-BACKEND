package indie.services.moduloUsuario;

import indie.models.moduloUsuario.FuncionalidadRol;
import indie.repositories.moduloUsuario.FuncionalidadRolRepository;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FuncionalidadRolService extends BaseServiceImpl<FuncionalidadRol, String> {

    FuncionalidadRolRepository funcionalidadRolRepository;

    public FuncionalidadRolService(FuncionalidadRolRepository funcionalidadRolRepository) {
        super(funcionalidadRolRepository);
        this.funcionalidadRolRepository = funcionalidadRolRepository;
    }

}
