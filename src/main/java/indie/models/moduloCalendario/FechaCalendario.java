package indie.models.moduloCalendario;

import com.fasterxml.jackson.annotation.JsonBackReference;
import indie.models.BaseModel;
import indie.models.moduloEventos.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "FechaCalendario")
public class FechaCalendario extends BaseModel {

    // === RELACIONES ===
    @ManyToOne
    @JoinColumn(name = "idCalendario")
    @JsonBackReference
    private Calendario idCalendario;
    
    @ManyToOne
    @JoinColumn(name = "idEvento")
    private Evento idEvento;  // NULL = Es un bloqueo, NOT NULL = Es un evento

    // === CAMPOS PARA BLOQUEOS ===
    // Solo se usan cuando idEvento = null (es decir, es un bloqueo)
    
    @Column(name = "fecha_desde")
    private LocalDate fechaDesde;

    @Column(name = "fecha_hasta")
    private LocalDate fechaHasta;

    @Column(name = "hora_desde")
    private LocalTime horaDesde;

    @Column(name = "hora_hasta")
    private LocalTime horaHasta;

    @Column(name = "todo_el_dia")
    @Builder.Default
    private Boolean todoElDia = false;

    @Column(name = "motivo")
    private String motivo;

    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Determina si esta fecha es un bloqueo (sin evento asociado)
     */
    public boolean esBloqueo() {
        return this.idEvento == null;
    }

    /**
     * Determina si esta fecha es un evento (con evento asociado)
     */
    public boolean esEvento() {
        return this.idEvento != null;
    }

    /**
     * Determina si esta fecha está activa (no eliminada)
     * Usa el deletedAt heredado de BaseModel
     */
    public boolean estaActivo() {
        return this.getDeletedAt() == null;
    }

    /**
     * Determina si esta fecha está bloqueada y activa
     */
    public boolean esBloqueaActivo() {
        return esBloqueo() && estaActivo();
    }
}
