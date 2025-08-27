package indie.security;

import indie.models.moduloUsuario.Usuario;
import indie.services.moduloUsuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll() // Solo login público
                        .requestMatchers("/api/auth/register").permitAll() // Solo register público
                        .requestMatchers("/api/auth/verify").permitAll() // Solo verificacion de email público
                        .requestMatchers("/api/public/**").permitAll() // Endpoints públicos específicos
                        .requestMatchers("/api/**").authenticated() // Resto requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new JwtAuthenticationFilter(authManager, jwtUtils))
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
                    if (u.getSubTipoUsuario() != null) {
                        role = u.getSubTipoUsuario().getNombreSubTipoUsuario();
                        System.out.println("✓ Role del usuario: " + role);
                    } else {
                        System.out.println("⚠ SubTipoUsuario es null, usando role por defecto: " + role);
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

        // Especificar orígenes exactos
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",  // Tu frontend Angular
                "http://localhost:3000"   // Por si usas otro puerto
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



