package indie.services.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.Mensaje;
import indie.models.moduloUsuario.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface MensajeService {
    Mensaje enviarMensaje(Chat chat, Usuario emisor, String contenido);
    List<Mensaje> listarMensajes(Chat chat);
    Optional<Mensaje> ultimoMensaje(Chat chat);
    long contarNoLeidos(Chat chat, Usuario para, LocalDateTime lastReadAt);
    Page<Mensaje> listarMensajes(Chat chat, Pageable pageable);
}
