package indie.dtos.busqueda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoBusquedaDTO {
    
    private List<UsuarioBusquedaDTO> usuarios;
    private List<EventoBusquedaDTO> eventos;
    private int totalUsuarios;
    private int totalEventos;
    private String terminoBuscado;
}