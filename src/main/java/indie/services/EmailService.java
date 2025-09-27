package indie.services;

import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import indie.repositories.moduloUsuario.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class EmailService {

    private static final String VERIFICATION_LOGO_DATA_URI = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MjAgMTQwIj4KICA8c3R5bGU+CiAgICAuYnJhbmQgeyBmb250LWZhbWlseTogIlNlZ29lIFVJIiwgQXJpYWwsIHNhbnMtc2VyaWY7IGZvbnQtd2VpZ2h0OiA4MDA7IH0KICA8L3N0eWxlPgogIDxyZWN0IGZpbGw9Im5vbmUiIHdpZHRoPSI0MjAiIGhlaWdodD0iMTQwIiAvPgogIDx0ZXh0IHg9IjAiIHk9IjkyIiBmb250LXNpemU9Ijg4IiBjbGFzcz0iYnJhbmQiIGZpbGw9IiNGNUU5RkYiPklORElFPC90ZXh0PgogIDxwYXRoIGQ9Ik0zMzIgMzggSDM2OCBMMzUwIDc0IEwzNjggMTEwIEgzMzIgTDMxNCA3NCBaIiBmaWxsPSIjQzU2N0ZGIiAvPgogIDx0ZXh0IHg9IjAiIHk9IjEzMCIgZm9udC1zaXplPSIzMCIgbGV0dGVyLXNwYWNpbmc9IjUiIGNsYXNzPSJicmFuZCIgZmlsbD0iI0M1NjdGRiI+REVTQ1VCUiYjMjA1OyBZIENPTkVDVCYjMTkzOzwvdGV4dD4KPC9zdmc+";

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

    @Value("http://localhost:4200/new-password?token=")
    private String resetPasswordUrl;

    public void enviarEmailVerificacion(String toEmail, String token) {
        String subject = "Verifica tu cuenta";
        String verificationLink = verificationUrl + token;

        String plainText = "Bienvenido a INDIE!" +
                "Por favor, verifica tu cuenta haciendo clic en el siguiente enlace:" +
                verificationLink + "" +
                "Este enlace expira en 24 horas." +
                "Gracias por registrarte!";

        String htmlContent = String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body style="margin:0;padding:0;background-color:#1E1624;font-family:'Segoe UI',Arial,sans-serif;color:#F5F2FF;">
  <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#1E1624;">
    <tr>
      <td align="center" style="padding:32px 16px;">
        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="max-width:560px;background-color:#261C38;border-radius:20px;box-shadow:0 20px 40px rgba(0,0,0,0.35);">
          <tr>
            <td align="center" style="padding:40px 40px 16px;">
              <img src="%s" alt="INDIE" width="200" style="display:block;width:200px;max-width:80%%;height:auto;">
            </td>
          </tr>
          <tr>
            <td style="padding:0 40px 24px;text-align:center;color:#F5F2FF;">
              <h1 style="margin:0 0 16px;font-size:26px;font-weight:700;">&iexcl;Confirma tu cuenta en INDIE!</h1>
              <p style="margin:0;font-size:16px;line-height:1.6;color:#D5C8E8;">
                Gracias por registrarte. Tu mundo musical est&aacute; a un clic de distancia. Haz clic en el bot&oacute;n para validar tu correo electr&oacute;nico y comenzar a descubrir.
              </p>
            </td>
          </tr>
          <tr>
            <td align="center" style="padding:0 40px 32px;">
              <a href="%s" style="background-color:#C567FF;color:#1E1624;text-decoration:none;font-weight:700;font-size:16px;padding:14px 32px;border-radius:30px;display:inline-block;">Verificar correo</a>
            </td>
          </tr>
          <tr>
            <td style="padding:0 40px 40px;text-align:center;color:#9C8AB9;font-size:14px;line-height:1.6;">
              <p style="margin:0 0 12px;">Si el bot&oacute;n no funciona, copia y pega este enlace en tu navegador:</p>
              <p style="margin:0;word-break:break-all;"><a href="%s" style="color:#C567FF;text-decoration:none;">%s</a></p>
              <p style="margin:24px 0 0;color:#7A6A95;font-size:12px;">Este enlace expira en 24 horas.</p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>
""", VERIFICATION_LOGO_DATA_URI, verificationLink, verificationLink, verificationLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(plainText, htmlContent);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el email de verificacion", e);
        }
    }

    public void enviarEmailRecuperacion(String toEmail) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmailUsuario(toEmail);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con email: " + toEmail);
        }

        String subject = "Recupera tu contrase\u00f1a";
        String token = UUID.randomUUID().toString();

        // Crear y guardar el token especifico para reseteo de contrasena
        tokenService.crearToken(usuario, token, VerificationToken.TokenType.RESETEO_CONTRASENA);

        String recoveryLink = resetPasswordUrl + token;

        String body = "Has solicitado recuperar tu contrase\u00f1a." +
                "Por favor, haz clic en el siguiente enlace para restablecer tu contrase\u00f1a" +
                recoveryLink + "" +
                "Este enlace expira en 24 horas." +
                "Si no solicitaste este cambio, puedes ignorar este correo.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
