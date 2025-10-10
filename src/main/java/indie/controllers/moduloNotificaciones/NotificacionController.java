package indie.controllers.moduloNotificaciones;

import indie.controllers.BaseController;
import indie.models.moduloNotificaciones.Notificacion;
import indie.models.moduloUsuario.Usuario;
import indie.services.moduloNotificaciones.NotificacionServiceImpl;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notificacion")
public class NotificacionController extends BaseController<Notificacion, String> {

    @Autowired
    protected NotificacionServiceImpl notificacionService;
    @Autowired
    protected UsuarioService usuarioService;

    public NotificacionController(NotificacionServiceImpl notificacionService) {
        super(notificacionService);
    }

    private static String extractToken(String content, String key) {
        int idx = content.indexOf(key);
        if (idx < 0) return null;
        int start = idx + key.length();
        int end = content.indexOf(' ', start);
        if (end < 0) end = content.length();
        String raw = content.substring(start, end).trim();
        // limpiar separadores comunes como '|' o ','
        if (raw.endsWith("|") || raw.endsWith(",")) raw = raw.substring(0, raw.length()-1);
        return raw;
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notificacion>> unread(@AuthenticationPrincipal String email) {
        Usuario me = usuarioService.buscarPorEmail(email).orElse(null);
        if (me == null) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(notificacionService.unreadFor(me));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> recent(@AuthenticationPrincipal String email,
                                                            @RequestParam(name = "limit", defaultValue = "6") int limit) {
        Usuario me = usuarioService.buscarPorEmail(email).orElse(null);
        if (me == null) return ResponseEntity.ok(List.of());
        var list = notificacionService.recentFor(me, Math.max(1, Math.min(limit, 12)));
        var out = list.stream().map(n -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", n.getId());
            m.put("contenidoNotificacion", n.getContenidoNotificacion());
            m.put("createdAt", n.getCreatedAt());
            m.put("tipo", n.getIdTipoNotificacion() != null ? n.getIdTipoNotificacion().getNombreTipoNotificacion() : "");
            // Resolver actionUrl basada en tokens opcionales en el contenido: evt:{id} y user:{username}
            String content = n.getContenidoNotificacion();
            String actionUrl = "/configuracion-profile/notificaciones"; // fallback
            try {
                if (content != null) {
                    String lower = content.toLowerCase();
                    String evtId = extractToken(content, "evt:");
                    String username = extractToken(content, "user:");
                    if (evtId != null) {
                        actionUrl = "/mis-eventos?open=" + evtId;
                    } else if (username != null) {
                        actionUrl = "/profile/" + username;
                    }
                }
            } catch (Exception ignored) {}
            m.put("actionUrl", actionUrl);
            return m;
        }).toList();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markRead(@AuthenticationPrincipal String email, @RequestBody Map<String, List<String>> body) {
        Usuario me = usuarioService.buscarPorEmail(email).orElse(null);
        if (me == null) return ResponseEntity.ok().build();
        List<String> ids = body != null ? body.getOrDefault("ids", List.of()) : List.of();
        notificacionService.markRead(me, ids);
        return ResponseEntity.ok().build();
    }
}
