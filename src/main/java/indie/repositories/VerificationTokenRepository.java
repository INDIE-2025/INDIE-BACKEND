package indie.repositories;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUsuarioAndTipo(Usuario usuario, VerificationToken.TokenType tipo);
    void deleteByUsuarioAndTipo(Usuario usuario, VerificationToken.TokenType tipo);
}
