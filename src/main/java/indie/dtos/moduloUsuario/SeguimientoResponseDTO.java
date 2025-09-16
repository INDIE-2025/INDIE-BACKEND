package indie.dtos.moduloUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoResponseDTO {
    
    private List<UsuarioSeguimientoDTO> usuarios;
    private long total;
    private String tipoLista; // "seguidores", "seguidos", "bloqueados"
}