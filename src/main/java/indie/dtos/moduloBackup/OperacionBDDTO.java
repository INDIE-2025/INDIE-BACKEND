package indie.dtos.moduloBackup;

import java.time.LocalDateTime;

import indie.models.enums.EstadoOperacion;
import indie.models.enums.TipoOperacion;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class OperacionBDDTO {

    private String nombreOperacion;
    private LocalDateTime fechaRealizacion;
    private String username;
    private String tipo;
    private String estado;

    public OperacionBDDTO(String nombreOperacion,
                          LocalDateTime fechaRealizacion,
                          String username,
                          TipoOperacion tipo,
                          EstadoOperacion estado) {
        this.nombreOperacion = nombreOperacion;
        this.fechaRealizacion = fechaRealizacion;
        this.username = username;
        this.tipo = tipo.toString();
        this.estado = estado.toString();
    }
}
