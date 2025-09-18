package indie.services.moduloUsuario;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.services.BaseServiceImpl;
import indie.dtos.auth.SubTipoUsuarioDTO;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SubTipoUsuarioService extends BaseServiceImpl<SubTipoUsuario, String> {

    SubTipoUsuarioRepository subTipoUsuarioRepository;

    public SubTipoUsuarioService(SubTipoUsuarioRepository subTipoUsuarioRepository) {
        super(subTipoUsuarioRepository);
        this.subTipoUsuarioRepository = subTipoUsuarioRepository;
    }

    public List<SubTipoUsuarioDTO> findAllSubTipo() {
        return subTipoUsuarioRepository.findAll().stream().map(subTipo -> {
            SubTipoUsuarioDTO dto = new SubTipoUsuarioDTO();
            dto.setId(subTipo.getId());
            dto.setNombreSubTipoUsuario(subTipo.getNombreSubTipoUsuario());
            dto.setNombreTipoUsuario(
                subTipo.getTipoUsuario() != null ? subTipo.getTipoUsuario().getNombreTipoUsuario() : null
            );
            dto.setDeletedAt(subTipo.getDeletedAt());
            return dto;
        }).toList();
    }
}
