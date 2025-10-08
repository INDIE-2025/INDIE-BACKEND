package indie.repositories.moduloMensajeria;

import indie.models.moduloMensajeria.Chat;
import indie.models.moduloMensajeria.ChatUsuario;
import indie.models.moduloUsuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUsuarioRepository extends JpaRepository<ChatUsuario, String> {

    List<ChatUsuario> findByIdUsuario(Usuario usuario);

    List<ChatUsuario> findByIdChat(Chat chat);

    @Query("SELECT cu1.idChat FROM ChatUsuario cu1 JOIN ChatUsuario cu2 ON cu1.idChat = cu2.idChat " +
            "WHERE cu1.idUsuario.id = :userA AND cu2.idUsuario.id = :userB")
    Optional<Chat> findDirectChatBetween(@Param("userA") String userA, @Param("userB") String userB);

    Optional<ChatUsuario> findByIdChatAndIdUsuario(Chat chat, Usuario usuario);
}
