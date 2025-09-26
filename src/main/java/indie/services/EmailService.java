package indie.services;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import indie.repositories.moduloUsuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.verification.url}")
    private String verificationUrl;

    @Value("${app.resetPassword.url}")
    private String resetPasswordUrl;

    public void enviarEmailVerificacion(String toEmail, String token) {
        String subject = "Verifica tu cuenta";
        String verificationLink = verificationUrl + token;

        String body = "¡Bienvenido!\n\n" +
                "Por favor, verifica tu cuenta haciendo clic en el siguiente enlace:\n" +
                verificationLink + "\n\n" +
                "Este link expirará en 24 horas.\n\n" +
                "Gracias por registrarte!";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void enviarEmailRecuperacion(String toEmail) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmailUsuario(toEmail);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con email: " + toEmail);
        }

        String subject = "Recupera tu contraseña";
        String token = UUID.randomUUID().toString();

        // Crear y guardar el token específico para reseteo de contraseña
        tokenService.crearToken(usuario, token, VerificationToken.TokenType.RESETEO_CONTRASENA);

        String recoveryLink = resetPasswordUrl + token;

        String body = "Has solicitado recuperar tu contraseña.\n\n" +
                "Por favor, haz clic en el siguiente enlace para restablecer tu contraseña:\n" +
                recoveryLink + "\n\n" +
                "Este link expirará en 24 horas.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
