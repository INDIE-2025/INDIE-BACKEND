package indie.services.moduloComentarios;

import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.repositories.moduloComentarios.ComentarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import indie.models.moduloUsuario.Usuario;

@Service
public class ComentarioServiceImpl extends BaseServiceImpl<ComentarUsuario,String> implements ComentarioService {

    ComentarioRepository comentarioRepository;
    UsuarioRepository usuarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository){
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public List<ComentarioDTO> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception {
        try {
            return comentarioRepository.traerComentariosDeUnUsuario(idUsuarioComentado);
        } catch (Exception e) {
            throw new Exception("Error al obtener comentarios: " + e.getMessage());
        }
    }

}
