package indie.services;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import indie.repositories.VerificationTokenRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Genera y guarda un token con tipo específico
    public VerificationToken crearToken(Usuario usuario, String token, VerificationToken.TokenType tipo) {
        // Eliminar token anterior del mismo tipo si existe
        tokenRepo.deleteByUsuarioAndTipo(usuario, tipo);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsuario(usuario);
        verificationToken.setTipo(tipo);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // válido 24 hs

        return tokenRepo.save(verificationToken);
    }

    public VerificationToken crearToken(Usuario usuario, String token) {
        return crearToken(usuario, token, VerificationToken.TokenType.VERIFICACION_CUENTA);
    }

    // Busca por token
    public Optional<VerificationToken> getByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    // Verifica el usuario
    public String verificarCuenta(String token) {
        VerificationToken vToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Verificar que sea un token de verificación de cuenta
        if (vToken.getTipo() != VerificationToken.TokenType.VERIFICACION_CUENTA) {
            throw new RuntimeException("Token no válido para verificación de cuenta");
        }

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "El token ha expirado";
        }

        Usuario usuario = vToken.getUsuario();
        if (usuario.isVerificado()) {
            return "La cuenta ya está verificada";
        }

        usuario.setFechaVerificacion(LocalDateTime.now());
        usuarioRepo.save(usuario);

        // Eliminar el token usado
        tokenRepo.delete(vToken);

        return "Cuenta activada con éxito";
    }

    // Valida si un token de recuperación es válido
    public boolean validarTokenRecuperacion(String token) {
        Optional<VerificationToken> tokenOpt = tokenRepo.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        VerificationToken vToken = tokenOpt.get();

        // Verificar que sea un token de reseteo de contraseña
        if (vToken.getTipo() != VerificationToken.TokenType.RESETEO_CONTRASENA) {
            return false;
        }

        return vToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public String resetearContrasena(String token, String nuevaContrasena) {
        VerificationToken vToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Verificar que sea un token de reseteo de contraseña
        if (vToken.getTipo() != VerificationToken.TokenType.RESETEO_CONTRASENA) {
            throw new RuntimeException("Token no válido para reseteo de contraseña");
        }

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "El token ha expirado";
        }

        Usuario usuario = vToken.getUsuario();

        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepo.save(usuario);

        tokenRepo.delete(vToken);

        return "Contraseña actualizada con éxito";
    }
}
