package indie.controllers.moduloUsuario;

import indie.dtos.moduloUsuario.admin.UsuarioAdminCreateDTO;
import indie.dtos.moduloUsuario.admin.UsuarioAdminResponseDTO;
import indie.dtos.moduloUsuario.admin.UsuarioAdminUpdateDTO;
import indie.services.moduloUsuario.UsuarioAdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final UsuarioAdminService usuarioAdminService;

    @GetMapping
    public ResponseEntity<List<UsuarioAdminResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioAdminService.obtenerTodos());
    }

    @GetMapping("/{email:.+}")
    public ResponseEntity<UsuarioAdminResponseDTO> obtenerUsuario(@PathVariable String email) {
        return ResponseEntity.ok(usuarioAdminService.obtenerPorEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsuarioAdminResponseDTO> crearUsuario(@Valid @RequestBody UsuarioAdminCreateDTO dto) {
        return ResponseEntity.ok(usuarioAdminService.crear(dto));
    }

    @PutMapping("/{email:.+}")
    public ResponseEntity<UsuarioAdminResponseDTO> actualizarUsuario(@PathVariable String email,
                                                                     @Valid @RequestBody UsuarioAdminUpdateDTO dto) {
        return ResponseEntity.ok(usuarioAdminService.actualizar(email, dto));
    }

}
