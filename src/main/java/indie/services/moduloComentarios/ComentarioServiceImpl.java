package indie.services.moduloComentarios;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.repositories.moduloComentarios.ComentarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl extends BaseServiceImpl<ComentarUsuario,String> implements ComentarioService {

    ComentarioRepository comentarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository){
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public List<ComentarUsuario> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception {
        try {
            List<ComentarUsuario> comentario = comentarioRepository.traerComentariosDeUnUsuario(idUsuarioComentado);
            return comentario;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
