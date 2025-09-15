package indie.dtos.moduloComentarios;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ComentarioDTO {
    
    private String idComentario;
    private String comentario;
    private String idUsuarioComentador;
    private LocalDateTime createdAt;
    private String nombreAutorComentario;


    public ComentarioDTO(String comentario, String nombreAutorComentario, LocalDateTime createdAt, String idUsuarioComentador) {
        this.comentario = comentario;
        this.nombreAutorComentario = nombreAutorComentario;
        this.createdAt = createdAt;
        this.idUsuarioComentador = idUsuarioComentador;
    }

    public ComentarioDTO(String idComentario, String comentario, String nombreAutorComentario, LocalDateTime createdAt, String idUsuarioComentador) {
        this.idComentario = idComentario;
        this.comentario = comentario;
        this.nombreAutorComentario = nombreAutorComentario;
        this.createdAt = createdAt;
        this.idUsuarioComentador = idUsuarioComentador;
    }
}

