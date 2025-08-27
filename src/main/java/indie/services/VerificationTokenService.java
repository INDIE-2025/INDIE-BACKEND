package indie.services;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import indie.repositories.VerificationTokenRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    // Genera y guarda un token
    public VerificationToken crearToken(Usuario usuario, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsuario(usuario);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // válido 24 hs

        return tokenRepo.save(verificationToken);
    }

    // Busca por token
    public Optional<VerificationToken> getByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    // Verifica el usuario
    public String verificarCuenta(String token) {
        VerificationToken vToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "El token ha expirado";
        }

        Usuario usuario = vToken.getUsuario();
        if (usuario.isVerificado()) {
            return "La cuenta ya está verificada";
        }

        usuario.setFechaVerificacion(LocalDateTime.now());
        usuarioRepo.save(usuario);

        return "Cuenta activada con éxito";
    }
}

