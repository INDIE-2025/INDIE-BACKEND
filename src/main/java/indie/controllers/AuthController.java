package indie.controllers;

import indie.dtos.auth.EmailRequest;
import indie.dtos.auth.MessageResponse;
import indie.dtos.auth.ResetPasswordRequest;
import indie.dtos.auth.SubTipoUsuarioDTO;
import indie.dtos.auth.TokenValidationResponse;
import indie.dtos.auth.RegistroUsuarioRequest;
import indie.exceptions.EmailYaRegistradoException;
import indie.models.moduloUsuario.Usuario;
import indie.services.EmailService;
import indie.services.VerificationTokenService;
import indie.services.moduloUsuario.SubTipoUsuarioService;
import indie.services.moduloUsuario.UsuarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private SubTipoUsuarioService subTipoUsuarioService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroUsuarioRequest request) {
        try {
            Usuario usuarioRegistrado = usuarioService.registrar(request);
            return ResponseEntity.ok(usuarioRegistrado);
        } catch (EmailYaRegistradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verificarCuenta(@RequestParam("token") String token) {
        String resultado = verificationTokenService.verificarCuenta(token);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.enviarEmailRecuperacion(emailRequest.getEmail());
            return ResponseEntity.ok("Email de recuperación enviado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        boolean valid = verificationTokenService.validarTokenRecuperacion(token);
        return ResponseEntity.ok(new TokenValidationResponse(valid));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String resultado = verificationTokenService.resetearContrasena(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse(resultado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/sub-tipo-todos")
    public List<SubTipoUsuarioDTO> getAllSubTipos() {
        return subTipoUsuarioService.findAllSubTipo();
    }

}
