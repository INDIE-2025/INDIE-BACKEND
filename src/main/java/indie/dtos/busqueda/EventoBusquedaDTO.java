package indie.dtos.busqueda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoBusquedaDTO {
    
    private String id;
    private String tituloEvento;
    private String descripcionEvento;
    private LocalDateTime fechaHoraEvento;
    private String ubicacionEvento;
    private String nombreOrganizador; // nombre del usuario que organizó el evento
    private String usernameOrganizador;
    // Podrías agregar una URL de imagen del evento si la tienes
}