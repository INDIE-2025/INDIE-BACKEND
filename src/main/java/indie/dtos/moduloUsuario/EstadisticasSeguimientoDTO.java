package indie.dtos.moduloUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasSeguimientoDTO {
    
    private String usuarioId;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String username;
    private long totalSeguidores;
    private long totalSeguidos;
}