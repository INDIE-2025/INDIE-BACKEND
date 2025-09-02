package indie.models.moduloBackUp;

import indie.models.BaseModel;
import indie.models.enums.EstadoOperacion;
import indie.models.enums.TipoOperacion;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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