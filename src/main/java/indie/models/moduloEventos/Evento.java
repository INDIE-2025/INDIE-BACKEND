package indie.models.moduloEventos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore  // Evita referencias circulares en JSON
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
    @JsonIgnore  // Evita referencias circulares en JSON
    @Builder.Default
    private List<Colaboracion> colaboraciones = new ArrayList<>();

    // === MÉTODOS PARA JSON ===
    
    /**
     * Obtiene el ID del usuario para serialización JSON (evita referencias circulares)
     */
    public String getUsuarioId() {
        return this.idUsuario != null ? this.idUsuario.getId() : null;
    }

    /**
     * Obtiene el nombre del usuario para serialización JSON
     */
    public String getUsuarioNombre() {
        return this.idUsuario != null ? this.idUsuario.getNombreUsuario() : null;
    }

    /**
     * Obtiene el username del usuario para serialización JSON
     */
    public String getUsername() {
        return this.idUsuario != null ? this.idUsuario.getUsername() : null;
    }
}
