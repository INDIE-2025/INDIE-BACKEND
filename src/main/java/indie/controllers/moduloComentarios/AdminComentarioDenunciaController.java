package indie.controllers.moduloComentarios;

import indie.dtos.moduloComentarios.admin.AdminComentariosDTO;
import indie.services.moduloComentarios.AdminComentarioDenunciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/denuncias")
@RequiredArgsConstructor
public class AdminComentarioDenunciaController {

    private final AdminComentarioDenunciaService adminComentarioDenunciaService;

    @GetMapping
    public ResponseEntity<AdminComentariosDTO> obtenerDenuncias() {
        return ResponseEntity.ok(adminComentarioDenunciaService.obtenerDenuncias());
    }
}
