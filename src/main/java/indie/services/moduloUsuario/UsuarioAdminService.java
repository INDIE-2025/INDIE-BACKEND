
package indie.services.moduloUsuario;

import indie.dtos.moduloUsuario.admin.UsuarioAdminCreateDTO;
import indie.dtos.moduloUsuario.admin.UsuarioAdminResponseDTO;
import indie.dtos.moduloUsuario.admin.UsuarioAdminUpdateDTO;
import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioAdminService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SubTipoUsuarioRepository subTipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioAdminResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    public UsuarioAdminResponseDTO obtenerPorEmail(String email) {
        String emailNormalizado = email.trim();
        Usuario usuario = usuarioRepository.findByEmailUsuarioIgnoreCase(emailNormalizado)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return toResponseDto(usuario);
    }

    @Transactional
    public UsuarioAdminResponseDTO crear(UsuarioAdminCreateDTO dto) {
        String emailNormalizado = dto.getEmail().trim();
        validarEmailDisponible(emailNormalizado);

        String tipoNombre = dto.getTipoUsuarioNombre().trim();
        TipoUsuario tipoUsuario = obtenerTipoUsuarioPorNombre(tipoNombre);
        String subTipoNombre = hasText(dto.getSubTipoUsuarioNombre()) ? dto.getSubTipoUsuarioNombre().trim() : null;

        SubTipoUsuario subTipoUsuario = null;

        if (subTipoNombre != null) {
            subTipoUsuario = obtenerSubTipoPorTipoYNombre(tipoUsuario, subTipoNombre);
        }

        String nombre = dto.getNombre().trim();
        String username = dto.getUsuario().trim();
        String apellido = Optional.ofNullable(dto.getApellido())
            .map(String::trim)
            .filter(ap -> !ap.isEmpty())
            .orElse(nombre);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setApellidoUsuario(apellido);
        usuario.setEmailUsuario(emailNormalizado);
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setDeletedAt(resolveDeletedAtFromEstado(dto.getEstado()));
        usuario.setSubTipoUsuario(subTipoUsuario);
        usuario.setFechaVerificacion(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponseDto(guardado, tipoUsuario);
    }

    @Transactional
    public UsuarioAdminResponseDTO actualizar(String email, UsuarioAdminUpdateDTO dto) {
        String emailRuta = email.trim();
        System.out.println("Email ruta: " + emailRuta);
        Usuario usuario = usuarioRepository.findByEmailUsuarioIgnoreCase(emailRuta)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        String emailNormalizado = dto.getEmail().trim();
        if (!usuario.getEmailUsuario().equalsIgnoreCase(emailNormalizado)) {
            validarEmailDisponible(emailNormalizado);
        }

        String tipoNombre = dto.getTipoUsuarioNombre().trim();
        TipoUsuario tipoUsuario = obtenerTipoUsuarioPorNombre(tipoNombre);
        String subTipoNombre = hasText(dto.getSubTipoUsuarioNombre()) ? dto.getSubTipoUsuarioNombre().trim() : null;

        SubTipoUsuario subTipoUsuario = null;

        if (subTipoNombre != null) {
            subTipoUsuario = obtenerSubTipoPorTipoYNombre(tipoUsuario, subTipoNombre);
        }

        String nombre = dto.getNombre().trim();
        String username = dto.getUsuario().trim();

        usuario.setNombreUsuario(nombre);
        usuario.setEmailUsuario(emailNormalizado);
        usuario.setUsername(username);
        usuario.setDeletedAt(resolveDeletedAtFromEstado(dto.getEstado()));
        usuario.setSubTipoUsuario(subTipoUsuario);
        if (usuario.getFechaVerificacion() == null) {
            usuario.setFechaVerificacion(LocalDateTime.now());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return toResponseDto(actualizado, tipoUsuario);
    }

    private void validarEmailDisponible(String email) {
        if (usuarioRepository.existsByEmailUsuarioIgnoreCase(email)) {
            throw new IllegalArgumentException("El email ya se encuentra registrado");
        }
    }

    private SubTipoUsuario obtenerSubTipoPorTipoYNombre(TipoUsuario tipoUsuario, String nombreSubTipo) {
        return subTipoUsuarioRepository
            .findByTipoUsuario_IdAndNombreSubTipoUsuarioIgnoreCase(tipoUsuario.getId(), nombreSubTipo)
            .orElseThrow(() -> new EntityNotFoundException(
                "El subtipo \"" + nombreSubTipo + "\" no pertenece al tipo \"" + tipoUsuario.getNombreTipoUsuario() + "\""));
    }

    private TipoUsuario obtenerTipoUsuarioPorNombre(String nombreTipo) {
        return tipoUsuarioRepository.findByNombreTipoUsuarioIgnoreCase(nombreTipo)
            .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado"));
    }

    private String estadoDesdeEntidad(Usuario usuario) {
        return usuario.getDeletedAt() == null ? "Activo" : "De baja";
    }

    private LocalDateTime resolveDeletedAtFromEstado(String estado) {
        if (estado == null) {
            return null;
        }
        String normalizado = estado.trim().toUpperCase();
        if (normalizado.equals("DE BAJA") || normalizado.equals("DE_BAJA") || normalizado.equals("INACTIVO")) {
            return LocalDateTime.now();
        }
        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private UsuarioAdminResponseDTO toResponseDto(Usuario usuario) {
        return toResponseDto(usuario, null);
    }

    private UsuarioAdminResponseDTO toResponseDto(Usuario usuario, TipoUsuario tipoUsuarioResolved) {
        SubTipoUsuario subTipo = usuario.getSubTipoUsuario();
        TipoUsuario tipo = tipoUsuarioResolved != null ? tipoUsuarioResolved : (subTipo != null ? subTipo.getTipoUsuario() : null);

        return UsuarioAdminResponseDTO.builder()
            .nombre(usuario.getNombreUsuario())
            .nombreUsuario(usuario.getUsername())
            .email(usuario.getEmailUsuario())
            .tipoUsuario(tipo != null ? tipo.getNombreTipoUsuario() : null)
            .estado(estadoDesdeEntidad(usuario))
            .build();
    }
}