package indie.services.moduloComentarios;

import indie.dtos.moduloComentarios.ComentarioDTO;
import indie.models.moduloComentarios.ComentarUsuario;
import indie.models.moduloComentarios.Denuncia;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloComentarios.ComentarioRepository;
import indie.repositories.moduloComentarios.DenunciaRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import indie.services.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class ComentarioServiceImpl extends BaseServiceImpl<ComentarUsuario,String> implements ComentarioService {

    ComentarioRepository comentarioRepository;
    UsuarioRepository usuarioRepository;

    DenunciaRepository denunciaRepository;

    private final NotificacionServiceImpl notificacionService;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, UsuarioRepository usuarioRepository, DenunciaRepository denunciaRepository, NotificacionServiceImpl notificacionService) {
        super(comentarioRepository);
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.denunciaRepository = denunciaRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    public List<ComentarioDTO> traerComentariosDeUnUsuario(String idUsuarioComentado) throws Exception {
        try {
            return comentarioRepository.traerComentariosDeUnUsuario(idUsuarioComentado);
        } catch (Exception e) {
            throw new Exception("Error al obtener comentarios: " + e.getMessage());
        }
    }

    @Override
    public ComentarioDTO realizarComentario(String comentario,
                                            String idUsuarioComentador,
                                            String idUsuarioComentado) throws Exception {
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

            ComentarUsuario comentarioGuardado = comentarioRepository.save(nuevoComentario);

            // Notificar al usuario comentado
            String contenido = usuarioComentador.getNombreUsuario() + " comento en tu perfil";
            notificacionService.crear(usuarioComentado, "Nuevo comentario", contenido);

        
            return new ComentarioDTO(
                    comentarioGuardado.getId(),                    
                    comentarioGuardado.getComentario(),            
                    String.valueOf(usuarioComentador.getNombreUsuario()), 
                    comentarioGuardado.getCreatedAt(),             
                    usuarioComentador.getId()                     
            );

        } catch (Exception e) {
            throw new Exception("Error al realizar el comentario: " + e.getMessage());
        }
    }


    @Override
    public void eliminarComentario(String idComentario, String idUsuario) throws Exception {
        ComentarUsuario comentario = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new Exception("Comentario no encontrado"));

        if (!comentario.getIdUsuarioComentador().getId().equals(idUsuario)) {
            throw new Exception("No tienes permisos para eliminar este comentario");
        }
        LocalDateTime fechaBaja = LocalDateTime.now();
        comentario.setDeletedAt(fechaBaja);
        comentarioRepository.save(comentario);

        List<Denuncia> denunciasActivas = denunciaRepository.findByIdComentarioIdAndDeletedAtIsNull(idComentario);
        if (!denunciasActivas.isEmpty()) {
            denunciasActivas.forEach(denuncia -> denuncia.setDeletedAt(fechaBaja));
            denunciaRepository.saveAll(denunciasActivas);
        }
    }

    @Override
    public void denunciarComentario(String idComentario, String idUsuario, String motivoDenuncia) throws Exception {
        try {
            ComentarUsuario comentario = comentarioRepository.findById(idComentario)
                    .orElseThrow(() -> new Exception("Comentario no encontrado"));

            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));

            Denuncia denuncia = new Denuncia();
            denuncia.setIdComentario(comentario);
            denuncia.setIdUsuario(usuario);
            denuncia.setMotivoDenuncia(motivoDenuncia);

            denunciaRepository.save(denuncia);
        } catch (Exception e) {
            throw new Exception("Error al denunciar el comentario: " + e.getMessage());
        }
    }


}


