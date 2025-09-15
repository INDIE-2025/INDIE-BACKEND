package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.enums.eventoEstado;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Evento extends BaseModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Usuario idUsuario;

    private String descripcionEvento;

    private LocalDateTime fechaHoraEvento;

    @NotNull
    private String tituloEvento;

    private String ubicacionEvento;

    @NotNull
    private LocalDateTime fechaAltaEvento;

    private LocalDateTime fechaBajaEvento;

    private LocalDateTime fechaModificacionEvento;

    @Enumerated
    @NotNull
    private eventoEstado estadoEvento;

    //private List<Usuario> colaboradores;

}
