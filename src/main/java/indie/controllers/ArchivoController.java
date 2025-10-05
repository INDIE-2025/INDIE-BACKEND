package indie.controllers;

import indie.models.Archivo;
import indie.models.enums.TipoClase;
import indie.models.moduloUsuario.Usuario;
import indie.services.ArchivoService;
import indie.services.moduloUsuario.UsuarioService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/archivo")
public class ArchivoController extends BaseController<Archivo, String> {

    private final ArchivoService archivoService;

    @Autowired
    private UsuarioService usuarioService;

    public ArchivoController(ArchivoService archivoService) {
        super(archivoService);
        this.archivoService = archivoService;
    }

    // ================= Contenido digital de usuario =================

    @PostMapping(value = "/mis-contenidos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirContenido(@AuthenticationPrincipal String email,
                                            @RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Archivo vacio"));
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten imagenes"));
            }
            if (file.getSize() > 10L * 1024 * 1024) { // 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(Map.of("error", "La imagen excede 10MB"));
            }

            var opt = usuarioService.buscarPorEmail(email);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
            }
            Usuario u = opt.get();

            Archivo ar = Archivo.builder()
                    .nombreArchivo(file.getOriginalFilename() != null ? file.getOriginalFilename() : "imagen")
                    .tipoClase(TipoClase.USUARIO)
                    .tipoArchivo(contentType)
                    .urlArchivo("")
                    .idObjeto(u.getId())
                    .build();
            ar.setContenido(file.getBytes());
            ar.setContentType(contentType);
            Archivo saved = archivoService.save(ar);

            // Set a convenience URL to fetch bytes
            saved.setUrlArchivo("/api/archivo/" + saved.getId() + "/bytes");
            archivoService.save(saved);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo subir el archivo"));
        }
    }

    @GetMapping("/mis-contenidos")
    public ResponseEntity<List<Archivo>> listarMisContenidos(@AuthenticationPrincipal String email) {
        var u = usuarioService.buscarPorEmail(email).orElse(null);
        if (u == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(archivoService.listarPorObjetoYClase(u.getId(), TipoClase.USUARIO));
    }

    @GetMapping("/por-usuario/{userId}")
    public ResponseEntity<List<Archivo>> listarPorUsuario(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(archivoService.listarPorObjetoYClase(userId, TipoClase.USUARIO));
    }

    @GetMapping("/{archivoId}/bytes")
    public ResponseEntity<byte[]> obtenerBytes(@PathVariable("archivoId") String archivoId) {
        try {
            Archivo ar = archivoService.findById(archivoId);
            if (ar == null || ar.getContenido() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            String ct = (ar.getContentType() != null && !ar.getContentType().isBlank())
                    ? ar.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(ct)).body(ar.getContenido());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/mis-contenidos/{archivoId}")
    public ResponseEntity<?> eliminarMio(@AuthenticationPrincipal String email,
                                         @PathVariable("archivoId") String archivoId) {
        var u = usuarioService.buscarPorEmail(email).orElse(null);
        if (u == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Archivo ar = archivoService.findById(archivoId);
        if (ar == null) return ResponseEntity.notFound().build();
        if (ar.getTipoClase() != TipoClase.USUARIO || !u.getId().equals(ar.getIdObjeto())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No autorizado"));
        }
        archivoService.deleteById(archivoId);
        return ResponseEntity.noContent().build();
    }

    // Actualizar metadatos de un archivo propio
    @PutMapping("/mis-contenidos/{archivoId}/meta")
    public ResponseEntity<?> actualizarMeta(@AuthenticationPrincipal String email,
                                            @PathVariable("archivoId") String archivoId,
                                            @RequestBody MetaRequest req) {
        var u = usuarioService.buscarPorEmail(email).orElse(null);
        if (u == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Archivo ar = archivoService.findById(archivoId);
        if (ar == null) return ResponseEntity.notFound().build();
        if (ar.getTipoClase() != TipoClase.USUARIO || !u.getId().equals(ar.getIdObjeto())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No autorizado"));
        }
        ar.setTitulo(req.titulo);
        ar.setDescripcion(req.descripcion);
        archivoService.save(ar);
        return ResponseEntity.ok(ar);
    }

    // Reordenar archivos propios
    @PutMapping("/mis-contenidos/reordenar")
    public ResponseEntity<?> reordenar(@AuthenticationPrincipal String email,
                                       @RequestBody OrdenRequest req) {
        var u = usuarioService.buscarPorEmail(email).orElse(null);
        if (u == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (req == null || req.orden == null) return ResponseEntity.badRequest().build();

        // Obtener todos los archivos del usuario
        List<Archivo> archivos = archivoService.listarPorObjetoYClase(u.getId(), TipoClase.USUARIO);
        Map<String, Integer> pos = new java.util.HashMap<>();
        for (int i = 0; i < req.orden.size(); i++) pos.put(req.orden.get(i), i);
        for (Archivo a : archivos) {
            Integer p = pos.get(a.getId());
            a.setOrden(p != null ? p : 999999); // los no incluidos al final
            archivoService.save(a);
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class MetaRequest { public String titulo; public String descripcion; }

    @Data
    public static class OrdenRequest { public List<String> orden; }
}
