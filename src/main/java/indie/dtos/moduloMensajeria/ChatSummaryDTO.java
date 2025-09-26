package indie.dtos.moduloMensajeria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSummaryDTO {
    private String chatId;
    private String otherUserId;
    private String otherUsername;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
}
