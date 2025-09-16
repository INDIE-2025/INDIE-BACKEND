package indie.services.moduloUsuario;

import indie.exceptions.EmailYaRegistradoException;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import indie.services.EmailService;
import indie.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import indie.models.moduloUsuario.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, String> {

    UsuarioRepository usuarioRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    VerificationTokenService verificationTokenService;
    
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario usuario) {

        Usuario usuarioGuardado;

        if (buscarPorEmail(usuario.getEmailUsuario()).isPresent()) {
            throw new EmailYaRegistradoException("Email ya registrado");
        }

        if (usuario.getApellidoUsuario() == null) {
            usuario.setApellidoUsuario("p");
        }

        // Encriptar la contrasena antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        try {
            usuarioGuardado = save(usuario);
        } catch (Exception error) {
            throw new RuntimeException("Error al guardar el usuario: " + error.getMessage());
        }

        String token = UUID.randomUUID().toString();
        verificationTokenService.crearToken(usuarioGuardado, token);
        emailService.enviarEmailVerificacion(usuarioGuardado.getEmailUsuario(), token);

        return usuarioGuardado;
    }

    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usuarioRepository.findByUsername(username));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuarioRepository.findByEmailUsuario(email));
    }

    public <T extends Usuario> T actualizarUsuario(T usuario) {
        return usuarioRepository.save(usuario);
    }

    public String cambiarPassword(String email, String currentPassword, String newPassword) {
        if (email == null || email.isBlank() || currentPassword == null || currentPassword.isBlank() || newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Datos invalidos para cambiar la contrasena");
        }

        Usuario usuario = buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new IllegalArgumentException("La contrasena actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        return "Contrasena actualizada correctamente";
    }
}
