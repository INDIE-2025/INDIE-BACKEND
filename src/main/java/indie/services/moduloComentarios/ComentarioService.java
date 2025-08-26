package indie.services.moduloComentarios;

import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.BaseService;

import java.util.List;

public interface ComentarioService extends BaseService<ComentarUsuario, String> {

    List<ComentarioDTO> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception;

   ComentarioDTO realizarComentario(String comentario, String idUsuarioComentador, String idUsuarioComentado) throws Exception;
}
