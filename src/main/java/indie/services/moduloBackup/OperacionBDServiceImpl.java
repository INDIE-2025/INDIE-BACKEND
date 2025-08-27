package indie.services.moduloBackup;

import org.springframework.stereotype.Service;

import indie.dtos.moduloBackup.OperacionBDCreateDto;
import indie.models.moduloBackUp.OperacionBD;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloBackup.OperacionBDRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;

@Service
public class OperacionBDServiceImpl extends BaseServiceImpl<OperacionBD, String> implements OperacionBDService {

    private final OperacionBDRepository operacionBDRepository;
    private final UsuarioRepository usuarioRepository;

    public OperacionBDServiceImpl(OperacionBDRepository operacionBDRepository, UsuarioRepository usuarioRepository) {
        super(operacionBDRepository);
        this.operacionBDRepository = operacionBDRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OperacionBD save(OperacionBDCreateDto dto) {
        
        Usuario usuario = usuarioRepository.findByEmailUsuario(dto.getUsuarioEmail());

        OperacionBD operacion = new OperacionBD();
        operacion.setNombreOperacion(dto.getNombreOperacion());
        operacion.setTipo(dto.getTipo());
        operacion.setEstado(dto.getEstado());
        operacion.setUsuario(usuario);

        System.out.println("Guardando operacion: " + operacion);

        return operacionBDRepository.save(operacion);
    }
}