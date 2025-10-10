package indie.dtos.moduloEventos;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public class crearEventoDTO {

    @NotBlank @Size(max = 120)
    public String titulo;

    @Size(max = 2000)
    public String descripcion;

    public LocalDateTime fechaHoraEvento;

    @Size(max = 255)
    public String ubicacion;

    @NotNull
    public String idUsuario;

    public String estadoEvento;
}
