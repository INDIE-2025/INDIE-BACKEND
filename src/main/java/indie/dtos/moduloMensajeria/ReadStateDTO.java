package indie.dtos.moduloMensajeria;

import java.time.LocalDateTime;

public record ReadStateDTO(String userId, LocalDateTime lastReadAt) {}

