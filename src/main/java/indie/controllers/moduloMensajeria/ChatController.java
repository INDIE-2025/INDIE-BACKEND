package indie.controllers.moduloMensajeria;

import indie.controllers.BaseController;
import indie.dtos.moduloMensajeria.ChatSummaryDTO;
import indie.dtos.moduloMensajeria.SendMessageRequest;
import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.Mensaje;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloMensajeria.ChatServiceImpl;
import indie.services.moduloMensajeria.MensajeService;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;
import indie.dtos.moduloMensajeria.ReadStateDTO;

@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController<Chat,String> {

    @Autowired
    protected ChatServiceImpl chatService;
    @Autowired
    protected MensajeService mensajeService;
    @Autowired
    protected UsuarioService usuarioService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatServiceImpl chatServiceImpl){
        super(chatServiceImpl);
    }

    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioService.buscarPorEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/with/{otherUserId}")
    public ResponseEntity<Chat> createOrGetDirectChat(@PathVariable("otherUserId") String otherUserId) {
        Usuario me = getCurrentUser();
        Usuario other = usuarioService.findById(otherUserId);
        Chat chat = chatService.getOrCreateDirectChat(me, other);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/mine")
    public ResponseEntity<java.util.List<ChatSummaryDTO>> listMyChats() {
        Usuario me = getCurrentUser();
        var chats = chatService.listarChatsDeUsuario(me);
        var dtos = chats.stream().map(chat -> {
            var lastOpt = mensajeService.ultimoMensaje(chat);
            var participantes = chatService.participantes(chat);
            Usuario other = participantes.stream().filter(u -> !u.getId().equals(me.getId())).findFirst().orElse(null);
            var cu = chatService.obtenerRelacion(chat, me);
            long unread = mensajeService.contarNoLeidos(chat, me, cu.getLastReadAt());
            return new ChatSummaryDTO(
                    chat.getId(),
                    other != null ? other.getId() : null,
                    other != null ? other.getUsername() : null,
                    lastOpt.map(Mensaje::getMensaje).orElse(null),
                    lastOpt.map(Mensaje::getCreatedAt).orElse(null),
                    unread
            );
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> listMessages(@PathVariable("chatId") String chatId,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "20") int size) {
        Usuario me = getCurrentUser();
        Chat chat = chatService.findById(chatId);
        // simple guard: ensure user is participant
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return ResponseEntity.status(403).build();
        // Ordenar por createdAt DESC para traer los más nuevos primero
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt"), Sort.Order.asc("id")));
        Page<Mensaje> mensajes = mensajeService.listarMensajes(chat, pageable);
        return ResponseEntity.ok(mensajes);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Mensaje> sendMessage(@PathVariable("chatId") String chatId, @org.springframework.web.bind.annotation.RequestBody SendMessageRequest body) {
        if (body == null || body.getMensaje() == null || body.getMensaje().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Usuario me = getCurrentUser();
        Chat chat = chatService.findById(chatId);
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return ResponseEntity.status(403).build();
        Mensaje saved = mensajeService.enviarMensaje(chat, me, body.getMensaje());
        // Broadcast por WebSocket
        try { messagingTemplate.convertAndSend("/topic/chat/" + chatId, saved); } catch (Exception ignored) {}
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{chatId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("chatId") String chatId) {
        Usuario me = getCurrentUser();
        Chat chat = chatService.findById(chatId);
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return ResponseEntity.status(403).build();
        chatService.marcarLeido(chat, me);
        try { messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/read", me.getId()); } catch (Exception ignored) {}
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<java.util.List<Usuario>> getParticipants(@PathVariable("chatId") String chatId) {
        Usuario me = getCurrentUser();
        Chat chat = chatService.findById(chatId);
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(chatService.participantes(chat));
    }

    @GetMapping("/{chatId}/read-state")
    public ResponseEntity<List<ReadStateDTO>> readState(@PathVariable("chatId") String chatId) {
        Usuario me = getCurrentUser();
        Chat chat = chatService.findById(chatId);
        boolean isParticipant = chatService.listarChatsDeUsuario(me).stream().anyMatch(c -> c.getId().equals(chatId));
        if (!isParticipant) return ResponseEntity.status(403).build();
        List<Usuario> parts = chatService.participantes(chat);
        List<ReadStateDTO> out = parts.stream().map(u -> {
            var cu = chatService.obtenerRelacion(chat, u);
            return new ReadStateDTO(u.getId(), cu.getLastReadAt());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }
}
