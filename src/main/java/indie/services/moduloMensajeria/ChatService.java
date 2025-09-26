package indie.services.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloUsuario.Usuario;
import indie.services.BaseService;

import java.util.List;

public interface ChatService extends BaseService<Chat, String> {
    Chat getOrCreateDirectChat(Usuario a, Usuario b);
    List<Chat> listarChatsDeUsuario(Usuario u);
    List<Usuario> participantes(Chat chat);
    indie.models.moduloMensajeria.ChatUsuario obtenerRelacion(Chat chat, Usuario u);
    void marcarLeido(Chat chat, Usuario u);
}
