package indie.controllers;

import indie.exceptions.EmailYaRegistradoException;
import indie.models.moduloUsuario.Usuario;
import indie.models.moduloUsuario.VerificationToken;
import indie.services.VerificationTokenService;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioRegistrado = usuarioService.registrar(usuario);
            return ResponseEntity.ok(usuarioRegistrado);
        } catch (EmailYaRegistradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verificarCuenta(@RequestParam String token) {
        String resultado = verificationTokenService.verificarCuenta(token);
        return ResponseEntity.ok(resultado);
    }

}


