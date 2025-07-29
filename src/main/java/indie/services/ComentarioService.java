package indie.services;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService extends BaseServiceImpl<ComentarUsuario,Long> {

    ComentarioRepository comentarioRepository;

    @Autowired
    public  ComentarioService(ComentarioRepository comentarioRepository){
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
    }
}
