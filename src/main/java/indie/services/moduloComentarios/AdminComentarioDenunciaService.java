package indie.services.moduloComentarios;

import indie.dtos.moduloComentarios.admin.AdminComentariosDTO;
import indie.repositories.moduloComentarios.DenunciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminComentarioDenunciaService {

    private final DenunciaRepository denunciaRepository;

    public AdminComentariosDTO obtenerDenuncias() {
        return new AdminComentariosDTO(denunciaRepository.findActiveComentarioDenuncias());
    }
}
