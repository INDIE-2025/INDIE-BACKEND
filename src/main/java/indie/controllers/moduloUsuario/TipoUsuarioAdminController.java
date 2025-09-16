package indie.controllers.moduloUsuario;

import indie.dtos.moduloUsuario.admin.TipoUsuarioAdminRequestDTO;
import indie.dtos.moduloUsuario.admin.TipoUsuarioAdminResponseDTO;
import indie.services.moduloUsuario.TipoUsuarioAdminService;
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
@RequestMapping("/api/admin/tipos-usuario")
@RequiredArgsConstructor
public class TipoUsuarioAdminController {

    private final TipoUsuarioAdminService tipoUsuarioAdminService;

    @GetMapping
    public ResponseEntity<List<TipoUsuarioAdminResponseDTO>> listarTiposUsuario() {
        return ResponseEntity.ok(tipoUsuarioAdminService.obtenerTodos());
    }

    @GetMapping("/{nombreTipoUsuario:.+}")
    public ResponseEntity<TipoUsuarioAdminResponseDTO> obtenerTipoUsuario(@PathVariable String nombreTipoUsuario) {
        return ResponseEntity.ok(tipoUsuarioAdminService.obtenerPorNombre(nombreTipoUsuario));
    }

    @PostMapping
    public ResponseEntity<TipoUsuarioAdminResponseDTO> crearTipoUsuario(@Valid @RequestBody TipoUsuarioAdminRequestDTO dto) {
        return ResponseEntity.ok(tipoUsuarioAdminService.crear(dto));
    }

    @PutMapping("/{nombreTipoUsuario:.+}")
    public ResponseEntity<TipoUsuarioAdminResponseDTO> actualizarTipoUsuario(@PathVariable String nombreTipoUsuario,
                                                                             @Valid @RequestBody TipoUsuarioAdminRequestDTO dto) {
        return ResponseEntity.ok(tipoUsuarioAdminService.actualizar(nombreTipoUsuario, dto));
    }

}
