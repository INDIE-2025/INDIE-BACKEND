package indie.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

import indie.models.moduloUsuario.Usuario;
import indie.services.moduloMensajeria.ChatServiceImpl;
import indie.services.moduloUsuario.UsuarioService;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private ChatServiceImpl chatService;
    @Autowired private UsuarioService usuarioService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                List<String> auth = accessor.getNativeHeader("Authorization");
                if (auth != null && !auth.isEmpty()) {
                    String header = auth.get(0);
                    if (header != null && header.startsWith("Bearer ")) {
                        String token = header.substring(7);
                        if (jwtUtils.validarToken(token)) {
                            String email = jwtUtils.extraerEmail(token);
                            var principal = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                            accessor.setUser(principal);
                        }
                    }
                }
            } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                String destination = accessor.getDestination();
                var user = accessor.getUser();
                if (destination != null && destination.startsWith("/topic/chat/") && user != null) {
                    String[] parts = destination.split("/");
                    // expected: "", "topic", "chat", "{chatId}" ["typing" or "read" optional]
                    if (parts.length >= 4) {
                        String chatId = parts[3];
                        String email = Objects.toString(user.getName(), null);
                        if (email != null) {
                            Usuario u = usuarioService.buscarPorEmail(email).orElse(null);
                            if (u != null) {
                                boolean allowed = chatService.listarChatsDeUsuario(u).stream().anyMatch(c -> c.getId().equals(chatId));
                                if (!allowed) {
                                    return null; // bloquea la suscripción a chats ajenos
                                }
                            }
                        }
                    }
                }
            }
        }
        return message;
    }
}
