package indie.models.moduloUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private TokenType tipo;

    public enum TokenType {
        VERIFICACION_CUENTA,
        RESETEO_CONTRASENA
    }
}
