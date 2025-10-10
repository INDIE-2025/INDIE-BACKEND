package indie.models.moduloReportes;

import indie.models.BaseModel;
import indie.models.moduloUsuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visualizacion_perfil")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizacionPerfil extends BaseModel {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_visitado_id", nullable = false)
    @NotNull
    private Usuario usuarioVisitado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_visitante_id")
    private Usuario usuarioVisitante; // Puede ser null para usuarios anónimos
    
    @NotNull
    @Column(name = "fecha_visualizacion")
    private LocalDateTime fechaVisualizacion;
    
    @Column(name = "direccion_ip", length = 45)
    private String direccionIp;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
}