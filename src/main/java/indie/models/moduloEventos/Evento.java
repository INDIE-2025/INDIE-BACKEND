package indie.models.moduloEventos;

import indie.models.BaseModel;
import indie.models.enums.eventoEstado;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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

    @OneToMany(mappedBy = "idEvento", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Colaboracion> colaboraciones = new ArrayList<>();
}
