package indie.dtos.moduloComentarios.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminComentarioDenunciaDTO {

    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String usuarioDenunciado;
    private String comentario;
    private String idComentario;
    private String motivoDenuncia;
    private String fechaDenuncia;
    private String usuarioDenunciante;

    public AdminComentarioDenunciaDTO(String usuarioDenunciado,
                                      String comentario,
                                      String idComentario,
                                      String motivoDenuncia,
                                      LocalDateTime fechaDenuncia,
                                      String usuarioDenunciante) {
        this.usuarioDenunciado = usuarioDenunciado;
        this.comentario = comentario;
        this.idComentario = idComentario;
        this.motivoDenuncia = motivoDenuncia;
        this.fechaDenuncia = fechaDenuncia != null ? fechaDenuncia.format(FECHA_FORMATTER) : null;
        this.usuarioDenunciante = usuarioDenunciante;
    }
}
