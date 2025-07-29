package indie.security;

import indie.models.moduloUsuario.Usuario;
import indie.services.UsuarioService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtUtils jwtUtils;
    private UsuarioService usuarioService;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtUtils jwtUtils, UsuarioService usuarioService) {
        super(authManager);
        this.jwtUtils = jwtUtils;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            try {
                chain.doFilter(request, response);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String token = header.replace("Bearer ", "");
        if (jwtUtils.validarToken(token)) {
            String email = jwtUtils.extraerEmail(token);
            Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);
            if (usuario != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        usuario.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        try {
            chain.doFilter(request, response);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}


