package indie.repositories.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, String> {

    List<Mensaje> findByIdChatOrderByCreatedAtAsc(Chat chat);

    Optional<Mensaje> findTopByIdChatOrderByCreatedAtDesc(Chat chat);

    long countByIdChatAndIdEmisorNot(Chat chat, indie.models.moduloUsuario.Usuario usuario);

    long countByIdChatAndCreatedAtAfterAndIdEmisorNot(Chat chat, LocalDateTime after, indie.models.moduloUsuario.Usuario usuario);

    Page<Mensaje> findByIdChat(Chat chat, Pageable pageable);
}
