package indie.services;

import indie.models.moduloComentarios.ComentarUsuario;

import java.util.List;

public interface ComentarioService extends BaseService<ComentarUsuario, Long>{

    List<ComentarUsuario> traerComentariosDeUnUsuario(Long idUsuarioComentado) throws Exception;
}
