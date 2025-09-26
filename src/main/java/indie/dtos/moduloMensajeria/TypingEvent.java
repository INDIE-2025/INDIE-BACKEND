package indie.dtos.moduloMensajeria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypingEvent {
    private String userId;
    private boolean typing;
}

