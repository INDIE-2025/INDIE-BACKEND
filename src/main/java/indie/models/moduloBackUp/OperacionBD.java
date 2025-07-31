package indie.models.moduloBackUp;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OperacionBD extends BaseModel {
    
    @NotNull
    private String nombreOperacion;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoOperacion tipo;
    
    @ManyToOne
    @NotNull
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoOperacion estado;
    
}