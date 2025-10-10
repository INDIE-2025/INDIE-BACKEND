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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrica_usuario")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricaUsuario extends BaseModel {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_metrica_id", nullable = false)
    @NotNull
    private TipoMetrica tipoMetrica;
    
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    
    @NotNull
    @Column(name = "fecha_metrica")
    private LocalDateTime fechaMetrica;
    
    @Column(name = "periodo_mes")
    private Integer periodoMes;
    
    @Column(name = "periodo_anio")
    private Integer periodoAnio;
    
    @Column(name = "metadatos", columnDefinition = "TEXT")
    private String metadatos; // JSON para información adicional específica de cada métrica
    
    // Métodos de utilidad
    public String getNombreMetrica() {
        return tipoMetrica != null ? tipoMetrica.getNombre() : null;
    }
    
    public String getUnidadMedida() {
        return tipoMetrica != null ? tipoMetrica.getUnidadMedida() : null;
    }
}