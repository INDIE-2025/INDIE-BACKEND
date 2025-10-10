package indie.controllers.moduloMensajeria;

import indie.dtos.moduloMensajeria.SendMessageRequest;
// Typing deshabilitado
// import indie.dtos.moduloMensajeria.TypingRequest;
// import indie.dtos.moduloMensajeria.TypingEvent;
import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.Mensaje;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloMensajeria.ChatServiceImpl;
import indie.services.moduloMensajeria.MensajeService;
import indie.services.moduloUsuario.UsuarioService;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWsController {

    @Autowired private MensajeService mensajeService;
    @Autowired private ChatServiceImpl chatService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private NotificacionServiceImpl notificacionService;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable String chatId, @Payload SendMessageRequest body, Principal principal) {
        if (body == null || body.getMensaje() == null || body.getMensaje().isBlank()) return;
        if (principal == null) return; // exigir usuario autenticado para evitar spam anónimo

        Usuario me = usuarioService.buscarPorEmail(principal.getName()).orElse(null);
        if (me == null) return;
        Chat chat = chatService.findById(chatId);
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return;

        Mensaje saved = mensajeService.enviarMensaje(chat, me, body.getMensaje());
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, saved);

        // Crear notificaciones simples para los demas participantes del chat
        chatService.participantes(chat).stream()
                .filter(u -> !u.getId().equals(me.getId()))
                .forEach(u -> notificacionService.crear(u, "Mensaje nuevo", me.getNombreUsuario() + " te envio un mensaje"));
    }

    // Typing deshabilitado para ahorrar recursos
    // @MessageMapping("/chat/{chatId}/typing")
    // public void typing(@DestinationVariable String chatId, @Payload TypingRequest body, Principal principal) {
    //     if (principal == null) return;
    //     Usuario me = usuarioService.buscarPorEmail(principal.getName()).orElse(null);
    //     if (me == null) return;
    //     Chat chat = chatService.findById(chatId);
    //     if (chat == null) return;
    //     boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
    //     if (!isParticipant) return;
    //     boolean typing = body != null && body.isTyping();
    //     messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/typing", new TypingEvent(me.getId(), typing));
    // }
}
