package indie.dtos.busqueda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioBusquedaDTO {
    
    private String id;
    private String username;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombreCompleto; // nombre + apellido
    private String emailUsuario;
    private String descripcionUsuario;
    // Podrías agregar una URL de foto de perfil si la tienes
}