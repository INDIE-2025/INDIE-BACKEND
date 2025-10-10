package indie.dtos.moduloEventos;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.*;

public class crearEventoDTO {
    
    // ID opcional para actualizar un borrador existente
    public String id;

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
    
    // Lista de IDs de usuarios colaboradores
    public List<String> colaboradores;
}
