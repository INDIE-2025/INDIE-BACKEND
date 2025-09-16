package indie.models.moduloUsuario;

import indie.models.BaseModel;
import indie.models.moduloCalendario.Calendario;
import indie.models.moduloEventos.Evento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario extends BaseModel {

    @NotNull
    private String nombreUsuario;
    
    private String apellidoUsuario;
    @NotNull
    private String emailUsuario;
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String descripcionUsuario;

    private LocalDateTime fechaVerificacion;

    private String youtubeUsuario;
    private String spotifyUsuario;
    private String instagramUsuario;

    @OneToOne(cascade = CascadeType.ALL)
    private Calendario calendario;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, optional = true)
    private SubTipoUsuario subTipoUsuario;

    public boolean isVerificado() {
        return fechaVerificacion != null;
    }

    @OneToMany(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore // evita recursion infinita al serializar JSON
    private List<Evento> eventos = new ArrayList<>();

}
