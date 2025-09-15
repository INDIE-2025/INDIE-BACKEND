package indie.repositories;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUsuarioAndTipo(Usuario usuario, VerificationToken.TokenType tipo);
    @Modifying
    @Transactional
    void deleteByUsuarioAndTipo(Usuario usuario, VerificationToken.TokenType tipo);
}
