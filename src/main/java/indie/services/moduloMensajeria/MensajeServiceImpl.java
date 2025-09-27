package indie.services.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.Mensaje;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloMensajeria.MensajeRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeServiceImpl(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @Override
    public Mensaje enviarMensaje(Chat chat, Usuario emisor, String contenido) {
        Mensaje m = Mensaje.builder()
                .idChat(chat)
                .idEmisor(emisor)
                .mensaje(contenido)
                .build();
        return mensajeRepository.save(m);
    }

    @Override
    public List<Mensaje> listarMensajes(Chat chat) {
        return mensajeRepository.findByIdChatOrderByCreatedAtAsc(chat);
    }

    @Override
    public Optional<Mensaje> ultimoMensaje(Chat chat) {
        return mensajeRepository.findTopByIdChatOrderByCreatedAtDesc(chat);
    }

    @Override
    public long contarNoLeidos(Chat chat, Usuario para, LocalDateTime lastReadAt) {
        if (lastReadAt == null) {
            return mensajeRepository.countByIdChatAndIdEmisorNot(chat, para);
        }
        return mensajeRepository.countByIdChatAndCreatedAtAfterAndIdEmisorNot(chat, lastReadAt, para);
    }

    @Override
    public Page<Mensaje> listarMensajes(Chat chat, Pageable pageable) {
        return mensajeRepository.findByIdChat(chat, pageable);
    }
}
