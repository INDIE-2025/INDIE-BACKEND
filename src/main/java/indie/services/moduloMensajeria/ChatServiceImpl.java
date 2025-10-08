package indie.services.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.ChatUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloMensajeria.ChatRepository;
import indie.repositories.moduloMensajeria.ChatUsuarioRepository;
import indie.services.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

@Service
public class ChatServiceImpl extends BaseServiceImpl<Chat,String> implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatUsuarioRepository chatUsuarioRepository;

    public ChatServiceImpl(ChatRepository chatRepository, ChatUsuarioRepository chatUsuarioRepository){
        super(chatRepository);
        this.chatRepository = chatRepository;
        this.chatUsuarioRepository = chatUsuarioRepository;
    }

    @Override
    public Chat getOrCreateDirectChat(Usuario a, Usuario b) {
        if (a == null || b == null) throw new IllegalArgumentException("Usuarios inválidos");
        if (Objects.equals(a.getId(), b.getId())) throw new IllegalArgumentException("No puedes chatear contigo mismo");

        return chatUsuarioRepository.findDirectChatBetween(a.getId(), b.getId())
                .orElseGet(() -> {
                    Chat nuevo = chatRepository.save(Chat.builder().atributo("direct").build());
                    ChatUsuario cuA = ChatUsuario.builder().idChat(nuevo).idUsuario(a).silenciado(false).build();
                    ChatUsuario cuB = ChatUsuario.builder().idChat(nuevo).idUsuario(b).silenciado(false).build();
                    chatUsuarioRepository.save(cuA);
                    chatUsuarioRepository.save(cuB);
                    return nuevo;
                });
    }

    @Override
    public List<Chat> listarChatsDeUsuario(Usuario u) {
        if (u == null) throw new EntityNotFoundException("Usuario no encontrado");
        return chatUsuarioRepository.findByIdUsuario(u).stream().map(ChatUsuario::getIdChat).toList();
    }

    @Override
    public List<Usuario> participantes(Chat chat) {
        return chatUsuarioRepository.findByIdChat(chat).stream().map(ChatUsuario::getIdUsuario).toList();
    }

    @Override
    public ChatUsuario obtenerRelacion(Chat chat, Usuario u) {
        return chatUsuarioRepository.findByIdChatAndIdUsuario(chat, u)
                .orElseThrow(() -> new EntityNotFoundException("Participación no encontrada"));
    }

    @Override
    public void marcarLeido(Chat chat, Usuario u) {
        ChatUsuario cu = obtenerRelacion(chat, u);
        cu.setLastReadAt(LocalDateTime.now());
        chatUsuarioRepository.save(cu);
    }
}
