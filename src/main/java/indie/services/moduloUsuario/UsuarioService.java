package indie.services.moduloUsuario;

import indie.exceptions.EmailYaRegistradoException;
import indie.dtos.auth.RegistroUsuarioRequest;
import indie.models.moduloCalendario.Calendario;
import indie.repositories.moduloUsuario.UsuarioRepository;
import indie.services.BaseServiceImpl;
import indie.services.EmailService;
import indie.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, String> {

    UsuarioRepository usuarioRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    SubTipoUsuarioService subTipoUsuarioService;

    @Autowired
    VerificationTokenService verificationTokenService;
    
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(RegistroUsuarioRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Solicitud de registro vacía");
        }

        if (buscarPorEmail(request.getEmailUsuario()).isPresent()) {
            throw new EmailYaRegistradoException("Email ya registrado");
        }

        if(findByUsername(request.getUsername()).isPresent()) {
            throw new EmailYaRegistradoException("El nombre de usuario ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoUsuario(request.getApellidoUsuario());
        usuario.setEmailUsuario(request.getEmailUsuario());
        usuario.setUsername(request.getUsername());
        usuario.setDescripcionUsuario(request.getDescripcionUsuario());
        usuario.setYoutubeUsuario(request.getYoutubeUsuario());
        usuario.setSpotifyUsuario(request.getSpotifyUsuario());
        usuario.setInstagramUsuario(request.getInstagramUsuario());
        // Encriptar la contrasena antes de guardar
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🆕 CREAR CALENDARIO AUTOMÁTICAMENTE
        Calendario calendario = new Calendario();
        calendario.setZonaHoraria("America/Argentina/Buenos_Aires"); // Zona horaria por defecto
        usuario.setCalendario(calendario);

        try {
            SubTipoUsuario subTipo = subTipoUsuarioService.findById(request.getSubTipoUsuarioId());
            usuario.setSubTipoUsuario(subTipo);
        } catch (Exception e) {
            throw new RuntimeException("Error al establecer el sub tipo de usuario: " + e.getMessage());
        }

        Usuario usuarioGuardado;
        try {
            usuarioGuardado = save(usuario);
        } catch (Exception error) {
            throw new RuntimeException("Error al guardar el usuario: " + error.getMessage());
        }

        String token = UUID.randomUUID().toString();
        verificationTokenService.crearToken(usuarioGuardado, token);
        emailService.enviarEmailVerificacion(usuarioGuardado.getEmailUsuario(), token);

        return usuarioGuardado;
    }

    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usuarioRepository.findByUsername(username));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuarioRepository.findByEmailUsuario(email));
    }

    public <T extends Usuario> T actualizarUsuario(T usuario) {
        return usuarioRepository.save(usuario);
    }

    public String cambiarPassword(String email, String currentPassword, String newPassword) {
        if (email == null || email.isBlank() || currentPassword == null || currentPassword.isBlank() || newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Datos invalidos para cambiar la contrasena");
        }

        Usuario usuario = buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new IllegalArgumentException("La contrasena actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        return "Contrasena actualizada correctamente";
    }
    
    /**
     * Busca usuarios por nombre de usuario parcial, ignorando mayúsculas/minúsculas
     * @param username Parte del nombre de usuario a buscar
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    public List<Usuario> buscarPorUsernameParcial(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        
        System.out.println("Servicio - Buscando usuarios con término: " + username);
        
        try {
            // Imprimir todos los usuarios que existen en la BD para diagnóstico
            List<Usuario> todosUsuarios = usuarioRepository.findAll();
            System.out.println("DIAGNÓSTICO - Total de usuarios en BD: " + todosUsuarios.size());
            for (Usuario u : todosUsuarios) {
                System.out.println("USUARIO EN BD: ID=" + u.getId() + 
                    ", Username=" + u.getUsername() + 
                    ", Nombre=" + u.getNombreUsuario() + 
                    ", Email=" + u.getEmailUsuario());
            }
            
            // Intentar búsqueda por cualquier campo
            System.out.println("Intentando búsqueda en cualquier campo");
            List<Usuario> usuarios = usuarioRepository.findByAnyField(username);
            System.out.println("Usuarios encontrados con findByAnyField: " + usuarios.size());
            
            // Si no hay resultados, intentar búsqueda directa con la consulta personalizada
            if (usuarios.isEmpty()) {
                System.out.println("Intentando búsqueda con JPA Query personalizada");
                usuarios = usuarioRepository.findByUsernameContainingIgnoreCase(username);
                System.out.println("Usuarios encontrados con JPA Query: " + usuarios.size());
            }
            
            // Si aún no hay resultados, intentar con el método derivado
            if (usuarios.isEmpty()) {
                System.out.println("No se encontraron resultados, intentando con método derivado");
                usuarios = usuarioRepository.findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(username);
                System.out.println("Usuarios encontrados con método derivado: " + usuarios.size());
            }
            
            // Si todavía no hay resultados, intentar una búsqueda manual
            if (usuarios.isEmpty()) {
                System.out.println("No se encontraron resultados, intentando búsqueda manual");
                
                usuarios = todosUsuarios.stream()
                    .filter(u -> 
                        (u.getUsername() != null && u.getUsername().toLowerCase().contains(username.toLowerCase())) ||
                        (u.getNombreUsuario() != null && u.getNombreUsuario().toLowerCase().contains(username.toLowerCase())) ||
                        (u.getApellidoUsuario() != null && u.getApellidoUsuario().toLowerCase().contains(username.toLowerCase())) ||
                        (u.getEmailUsuario() != null && u.getEmailUsuario().toLowerCase().contains(username.toLowerCase()))
                    )
                    .toList();
                
                System.out.println("Usuarios encontrados con filtro manual: " + usuarios.size());
                usuarios.forEach(u -> System.out.println("Usuario encontrado manual: " + u.getUsername()));
            } else {
                usuarios.forEach(u -> System.out.println("Usuario encontrado: " + u.getUsername()));
            }
            
            // Filtrar propiedades sensibles antes de devolver
            usuarios.forEach(usuario -> {
                usuario.setPassword(null);  // No enviar contraseñas
                usuario.setEventos(null);   // No enviar eventos para evitar recursión
            });
            
            return usuarios;
        } catch (Exception e) {
            // Log the exception
            System.out.println("Error en búsqueda de usuarios: " + e.getMessage());
            e.printStackTrace();
            // Return empty list instead of throwing to avoid 500 errors
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene una lista de usuarios de tipo artista que no han sido dados de baja,
     * ordenados por fecha de creación descendente y limitados a cierta cantidad.
     * 
     * @param limit Número máximo de resultados a devolver
     * @return Lista de artistas ordenados por fecha de creación más reciente
     */
    public List<Usuario> obtenerArtistasRecientes(int limit) {
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
            List<Usuario> artistas = usuarioRepository.findLatestArtistas(pageable);
            
            // Eliminar propiedades sensibles antes de devolver
            artistas.forEach(artista -> {
                artista.setPassword(null);  // No enviar contraseña
                artista.setFotoPerfil(null);  // No enviar foto (se obtiene por separado)
                artista.setEventos(null);   // No enviar eventos para evitar recursión
            });
            
            return artistas;
        } catch (Exception e) {
            System.out.println("Error al obtener artistas recientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
