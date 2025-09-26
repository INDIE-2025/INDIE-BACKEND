package indie.security;

import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Endpoints de autenticación
                        .requestMatchers("/api/public/**").permitAll() // Endpoints públicos específicos
                        .requestMatchers("/api/eventos/**").permitAll() // Endpoints para dev
                        .requestMatchers("/api/admin/**").permitAll() // Endpoints para dev
                        .requestMatchers("/api/chat/**").authenticated() // Endpoints privados
                        .requestMatchers("/ws/**").permitAll() // Handshake WebSocket
                        .requestMatchers("/actuator/health").permitAll() // Permitir acceso al endpoint de health
                        .requestMatchers("/error").permitAll() // Permitir acceso a la página de error
                       .requestMatchers("/api/**").authenticated() // Resto requiere autenticación
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new JwtAuthenticationFilter(authManager, jwtUtils, usuarioService))
                .addFilter(new JwtAuthorizationFilter(authManager, jwtUtils, usuarioService));
      
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(email -> {
                    System.out.println("=== UserDetailsService ===");
                    System.out.println("Buscando usuario: " + email);

                    Usuario u = usuarioService.buscarPorEmail(email).orElse(null);
                    if (u == null) {
                        System.out.println("✗ Usuario no encontrado");
                        throw new UsernameNotFoundException("Usuario no encontrado");
                    }

                    System.out.println("✓ Usuario encontrado: " + u.getEmailUsuario());
                    System.out.println("✓ Password hash presente: " + (u.getPassword() != null && !u.getPassword().isEmpty()));


                    // VALIDAR CUENTA VERIFICADA
                    if (u.getFechaVerificacion() == null) {
                        System.out.println("✗ Cuenta no verificada - fechaVerificacion es null");
                        throw new UsernameNotFoundException("Cuenta no verificada. Por favor verifica tu email.");
                    }

                    System.out.println("✓ Cuenta verificada - fechaVerificacion: " + u.getFechaVerificacion());
                    System.out.println("✓ Password hash presente: " + (u.getPassword() != null && !u.getPassword().isEmpty()));


                    String role = "USER";
                    if (u.getSubTipoUsuario() != null && u.getSubTipoUsuario().getNombreSubTipoUsuario() != null) {
                        role = u.getSubTipoUsuario().getNombreSubTipoUsuario();
                        System.out.println("✓ Role del usuario: " + role);
                    } else {
                        System.out.println("⚠ SubTipoUsuario es null o sin nombre, usando role por defecto: " + role);
                    }

                    User userDetails = new User(u.getEmailUsuario(), u.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                    System.out.println("✓ UserDetails creado exitosamente");
                    return userDetails;
                })
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Leer orígenes permitidos desde propiedades/entorno (coma-separados)
        List<String> allowedOrigins = Arrays.stream(corsAllowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        allowedOrigins.add("https://indie-frontend.vercel.app");
        allowedOrigins.add("http://localhost:4200");

        // Usar patrones para aceptar orígenes exactos o con comodines
        configuration.setAllowedOriginPatterns(allowedOrigins);

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // PasswordEncoder bean moved to separate config to avoid circular deps
}



