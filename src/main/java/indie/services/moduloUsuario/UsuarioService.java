package indie.services.moduloUsuario;

import indie.exceptions.EmailYaRegistradoException;
import indie.dtos.auth.RegistroUsuarioRequest;
import indie.models.moduloCalendario.Calendario;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import indie.services.EmailService;
import indie.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, String> {

    UsuarioRepository usuarioRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    SubTipoUsuarioService subTipoUsuarioService;

    @Autowired
    VerificationTokenService verificationTokenService;
    
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(RegistroUsuarioRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Solicitud de registro vacía");
        }

        if (buscarPorEmail(request.getEmailUsuario()).isPresent()) {
            throw new EmailYaRegistradoException("Email ya registrado");
        }

        if(findByUsername(request.getUsername()).isPresent()) {
            throw new EmailYaRegistradoException("El nombre de usuario ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoUsuario(request.getApellidoUsuario());
        usuario.setEmailUsuario(request.getEmailUsuario());
        usuario.setUsername(request.getUsername());
        usuario.setDescripcionUsuario(request.getDescripcionUsuario());
        usuario.setYoutubeUsuario(request.getYoutubeUsuario());
        usuario.setSpotifyUsuario(request.getSpotifyUsuario());
        usuario.setInstagramUsuario(request.getInstagramUsuario());
        // Encriptar la contrasena antes de guardar
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // CREAR CALENDARIO AUTOMÁTICAMENTE
        Calendario calendario = new Calendario();
        calendario.setZonaHoraria("America/Argentina/Buenos_Aires"); // Zona horaria por defecto
        usuario.setCalendario(calendario);

        try {
            SubTipoUsuario subTipo = subTipoUsuarioService.findById(request.getSubTipoUsuarioId());
            usuario.setSubTipoUsuario(subTipo);
        } catch (Exception e) {
            throw new RuntimeException("Error al establecer el sub tipo de usuario: " + e.getMessage());
        }

        Usuario usuarioGuardado;
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
