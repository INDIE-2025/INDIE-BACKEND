package indie.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import indie.models.moduloUsuario.Usuario;
import indie.dtos.auth.LoginResponse;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private indie.services.moduloUsuario.UsuarioService usuarioService;

    public JwtAuthenticationFilter(AuthenticationManager authManager, JwtUtils jwtUtils, indie.services.moduloUsuario.UsuarioService usuarioService) {
        this.authenticationManager = authManager;
        this.jwtUtils = jwtUtils;
        this.usuarioService = usuarioService;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            System.out.println("=== INICIO attemptAuthentication ===");

            Usuario creds = null;
            try {
                creds = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
                System.out.println("✓ JSON parseado - Email: " + creds.getEmailUsuario());
                System.out.println("✓ Password presente: " + (creds.getPassword() != null && !creds.getPassword().isEmpty()));
            } catch (java.io.IOException e) {
                System.out.println("✗ Error parseando JSON: " + e.getMessage());
                throw new RuntimeException(e);
            }

            System.out.println("Intentando autenticar con AuthenticationManager...");

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmailUsuario(), creds.getPassword())
            );

            System.out.println("✓ Autenticación exitosa");
            System.out.println("✓ Principal: " + auth.getPrincipal().getClass().getSimpleName());
            System.out.println("✓ Authorities: " + auth.getAuthorities());

            return auth;

        } catch (AuthenticationException e) {
            System.out.println("✗ Error de autenticación: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("✗ Error inesperado: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // UsuarioService se inyectó a nivel de clase
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        try {
            System.out.println("=== INICIO successfulAuthentication ===");

            String email = ((User) auth.getPrincipal()).getUsername();
            System.out.println("✓ Email del principal: " + email);
            
            // Buscar información del usuario
            indie.models.moduloUsuario.Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Generar token con toda la información necesaria
            String token = jwtUtils.generarToken(
                email, 
                usuario.getId(),  // getId() devuelve Long desde BaseModel
                usuario.getNombreUsuario(),
                usuario.getApellidoUsuario()
            );
            
            System.out.println("✓ Token generado: " + token.substring(0, 20) + "...");

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            
            // Crear respuesta con información del usuario
            LoginResponse response = 
                LoginResponse.fromUsuarioAndToken(usuario, token);
                
            // Convertir a JSON usando ObjectMapper
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            res.getWriter().write(jsonResponse);
            res.getWriter().flush();

            System.out.println("✓ Respuesta enviada exitosamente");

        } catch (java.io.IOException e) {
            System.out.println("✗ Error enviando respuesta: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException, java.io.IOException {
        System.out.println("=== unsuccessfulAuthentication ===");
        System.out.println("✗ Razón: " + failed.getClass().getSimpleName() + " - " + failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorMessage = "Credenciales inválidas";

        // Mensaje específico para cuenta no verificada
        if (failed.getMessage().contains("no verificada")) {
            errorMessage = "Cuenta no verificada. Por favor verifica tu email antes de iniciar sesión.";
        }

        String jsonResponse = "{\"error\": \"" + errorMessage + "\"}";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

