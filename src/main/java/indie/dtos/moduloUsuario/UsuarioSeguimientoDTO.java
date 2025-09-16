package indie.dtos.moduloUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSeguimientoDTO {
    
    private String id;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String username;
    
    // Estadísticas de seguimiento
    private long totalSeguidores;
    private long totalSeguidos;
    
    // Estado de relación con el usuario autenticado
    private boolean sigueAUsuarioActual;
    private boolean usuarioActualLoSigue;
    private boolean bloqueadoPorUsuarioActual;
}