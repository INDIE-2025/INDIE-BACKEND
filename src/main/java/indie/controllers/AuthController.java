package indie.controllers;

import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioService.buscarPorEmail(usuario.getEmailUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ya registrado");
        }
//        usuario.setSubTipoUsuario();
        return ResponseEntity.ok(usuarioService.registrar(usuario));
    }
}


