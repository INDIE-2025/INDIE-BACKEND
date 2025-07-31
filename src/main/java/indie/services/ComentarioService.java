package indie.services;

import indie.models.moduloComentarios.ComentarUsuario;

import java.util.List;

public interface ComentarioService extends BaseService<ComentarUsuario, String>{

    List<ComentarUsuario> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception;
}
