package indie.models.moduloCalendario;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import indie.models.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter // incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Calendario extends BaseModel {
    private String zonaHoraria;

    @OneToMany(mappedBy = "idCalendario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<FechaCalendario> fechasCalendario = new java.util.ArrayList<>();

}
