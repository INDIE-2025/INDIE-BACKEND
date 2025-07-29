package indie.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import indie.models.moduloUsuario.Usuario;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter(AuthenticationManager authManager, JwtUtils jwtUtils) {
        this.authenticationManager = authManager;
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Usuario creds = null;
            try {
                creds = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        String email = ((User) auth.getPrincipal()).getUsername();
        String token = jwtUtils.generarToken(email);
        res.setContentType("application/json");
        try {
            res.getWriter().write("{\"token\": \"" + token + "\"}");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        try {
            res.getWriter().flush();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}

