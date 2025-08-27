package indie.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.verification.url}")
    private String verificationUrl;

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
}
