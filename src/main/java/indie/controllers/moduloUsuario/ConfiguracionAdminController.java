package indie.controllers.moduloUsuario;

import indie.dtos.moduloUsuario.admin.ConfiguracionAdminRequestDTO;
import indie.services.moduloUsuario.ConfiguracionAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/configuracion")
@RequiredArgsConstructor
public class ConfiguracionAdminController {

    private final ConfiguracionAdminService configuracionAdminService;

    @GetMapping
    public ResponseEntity<ConfiguracionAdminRequestDTO> obtenerConfiguracion() {
        return ResponseEntity.ok(configuracionAdminService.obtenerConfiguracion());
    }

    @PutMapping
    public ResponseEntity<Void> actualizarConfiguracion(@Valid @RequestBody ConfiguracionAdminRequestDTO dto) {
        configuracionAdminService.actualizarConfiguracion(dto);
        return ResponseEntity.noContent().build();
    }
}
