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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@Setter
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
