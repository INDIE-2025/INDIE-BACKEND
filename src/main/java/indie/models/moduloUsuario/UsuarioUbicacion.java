package indie.models.moduloUsuario;

import indie.models.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UsuarioUbicacion extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @NotNull
    @Column(length = 500)
    private String direccion; // Texto legible tipo "Ciudad, País" o dirección completa

    private Double latitud;
    private Double longitud;
}
