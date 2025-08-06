package indie.services.moduloComentarios;

import indie.models.moduloComentarios.ComentarUsuario;
import indie.services.BaseService;

import java.util.List;

public interface ComentarioService extends BaseService<ComentarUsuario, String> {

    List<ComentarUsuario> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception;
}
