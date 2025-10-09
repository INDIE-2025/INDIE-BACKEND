package indie.dtos.moduloEventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagenEventoDTO {
    private String id;
    private String url;
    private String tipo;
}