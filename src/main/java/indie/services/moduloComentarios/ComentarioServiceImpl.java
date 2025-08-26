package indie.services.moduloComentarios;

import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloComentarios.ComentarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ComentarioServiceImpl extends BaseServiceImpl<ComentarUsuario,String> implements ComentarioService {

    ComentarioRepository comentarioRepository;
    UsuarioRepository usuarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, UsuarioRepository usuarioRepository){
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<ComentarioDTO> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception {
        try {
            List<ComentarioDTO> comentarios = comentarioRepository.traerComentariosDeUnUsuario(idUsuarioComentado);
            return comentarios.isEmpty() ? List.of() : comentarios; // Devuelve una lista vacía si no hay comentarios
        } catch (Exception e) {
            throw new Exception("Error al obtener comentarios: " + e.getMessage());
        }
    }

    @Override
    public void realizarComentario(String comentario, String idUsuarioComentador, String idUsuarioComentado) throws Exception {
        try {
            Usuario usuarioComentador = usuarioRepository.findById(idUsuarioComentador)
                    .orElseThrow(() -> new Exception("Usuario comentador no encontrado"));
            Usuario usuarioComentado = usuarioRepository.findById(idUsuarioComentado)
                    .orElseThrow(() -> new Exception("Usuario comentado no encontrado"));

            ComentarUsuario nuevoComentario = ComentarUsuario.builder()
                    .comentario(comentario)
                    .idUsuarioComentador(usuarioComentador)
                    .idUsuarioComentado(usuarioComentado)
                    .build();

            comentarioRepository.save(nuevoComentario);
        } catch (Exception e) {
            throw new Exception("Error al realizar el comentario: " + e.getMessage());
        }
    }

}
