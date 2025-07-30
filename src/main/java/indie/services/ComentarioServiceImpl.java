package indie.services;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl extends BaseServiceImpl<ComentarUsuario,Long> implements ComentarioService{

    ComentarioRepository comentarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository){
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public List<ComentarUsuario> traerComentariosDeUnUsuario(Long idUsuarioComentado) throws Exception {
        try {
            List<ComentarUsuario> comentario = comentarioRepository.traerComentariosDeUnUsuario(idUsuarioComentado);
            return comentario;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }




}
