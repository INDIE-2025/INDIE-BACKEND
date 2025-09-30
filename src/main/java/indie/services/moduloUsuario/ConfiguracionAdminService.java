package indie.services.moduloUsuario;

import indie.dtos.moduloUsuario.admin.ConfiguracionAdminRequestDTO;
import indie.models.moduloUsuario.ConfiguracionSistema;
import indie.models.moduloUsuario.Permiso;
import indie.models.moduloUsuario.PermisoTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import indie.repositories.moduloUsuario.ConfiguracionSistemaRepository;
import indie.repositories.moduloUsuario.PermisoRepository;
import indie.repositories.moduloUsuario.PermisoTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracionAdminService {

    private static final String NOMBRE_TIPO_ADMIN = "Administrador";

    private final ConfiguracionSistemaRepository configuracionSistemaRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PermisoRepository permisoRepository;
    private final PermisoTipoUsuarioRepository permisoTipoUsuarioRepository;

    @Transactional(readOnly = true)
    public ConfiguracionAdminRequestDTO obtenerConfiguracion() {
        ConfiguracionAdminRequestDTO dto = new ConfiguracionAdminRequestDTO();

        ConfiguracionAdminRequestDTO.ConfiguracionWrapper configuraciones = new ConfiguracionAdminRequestDTO.ConfiguracionWrapper();
        List<ConfiguracionAdminRequestDTO.ItemConfiguracionDTO> items = configuracionSistemaRepository
            .findAllByDeletedAtIsNullOrderByNombreConfiguracionAsc()
            .stream()
            .map(this::toItemConfiguracionDTO)
            .collect(Collectors.toList());
        configuraciones.setItems(items);
        dto.setConfiguraciones(configuraciones);

        ConfiguracionAdminRequestDTO.PermisosWrapper permisosWrapper = new ConfiguracionAdminRequestDTO.PermisosWrapper();
        List<ConfiguracionAdminRequestDTO.PermisoPorUsuarioDTO> usuarios = new ArrayList<>();
        List<Permiso> permisos = permisoRepository.findAllByOrderByNombreConfiguracionAsc();

        for (TipoUsuario tipoUsuario : tipoUsuarioRepository.findAllByDeletedAtIsNull()) {
            if (esAdministrador(tipoUsuario)) {
                continue;
            }

            ConfiguracionAdminRequestDTO.PermisoPorUsuarioDTO permisoPorUsuarioDTO = new ConfiguracionAdminRequestDTO.PermisoPorUsuarioDTO();
            permisoPorUsuarioDTO.setTipoUsuario(tipoUsuario.getNombreTipoUsuario());

            Map<String, PermisoTipoUsuario> permisosAsignados = permisoTipoUsuarioRepository.findByTipoUsuario(tipoUsuario)
                .stream()
                .collect(Collectors.toMap(
                    ptu -> ptu.getPermiso().getId(),
                    Function.identity(),
                    (existing, ignored) -> existing));

            List<ConfiguracionAdminRequestDTO.PermisoEstadoDTO> permisosDTO = new ArrayList<>();
            for (Permiso permiso : permisos) {
                ConfiguracionAdminRequestDTO.PermisoEstadoDTO permisoEstadoDTO = new ConfiguracionAdminRequestDTO.PermisoEstadoDTO();
                permisoEstadoDTO.setNombre(permiso.getNombreConfiguracion());
                PermisoTipoUsuario permisoTipoUsuario = permisosAsignados.get(permiso.getId());
                permisoEstadoDTO.setActivo(permisoTipoUsuario != null && permisoTipoUsuario.isActivo());
                permisosDTO.add(permisoEstadoDTO);
            }

            permisoPorUsuarioDTO.setPermisos(permisosDTO);
            usuarios.add(permisoPorUsuarioDTO);
        }

        permisosWrapper.setUsuarios(usuarios);
        dto.setPermisos(permisosWrapper);

        return dto;
    }

    @Transactional
    public void actualizarConfiguracion(ConfiguracionAdminRequestDTO dto) {
        actualizarConfiguraciones(dto.getConfiguraciones().getItems());
        actualizarPermisos(dto.getPermisos().getUsuarios());
    }

    private boolean esAdministrador(TipoUsuario tipoUsuario) {
        return tipoUsuario.getNombreTipoUsuario() != null
            && tipoUsuario.getNombreTipoUsuario().trim().equalsIgnoreCase(NOMBRE_TIPO_ADMIN);
    }

    private ConfiguracionAdminRequestDTO.ItemConfiguracionDTO toItemConfiguracionDTO(ConfiguracionSistema configuracion) {
        ConfiguracionAdminRequestDTO.ItemConfiguracionDTO dto = new ConfiguracionAdminRequestDTO.ItemConfiguracionDTO();
        dto.setNombre(configuracion.getNombreConfiguracion());
        dto.setActivo(configuracion.isActivo());
        dto.setValor(configuracion.getValor());
        return dto;
    }

    private void actualizarConfiguraciones(List<ConfiguracionAdminRequestDTO.ItemConfiguracionDTO> items) {
        for (ConfiguracionAdminRequestDTO.ItemConfiguracionDTO item : items) {
            String nombre = item.getNombre().trim();
            ConfiguracionSistema configuracion = configuracionSistemaRepository
                .findByNombreConfiguracionIgnoreCase(nombre)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Configuracion del sistema no encontrada: " + nombre));

            configuracion.setActivo(Boolean.TRUE.equals(item.getActivo()));
            if (item.getValor() != null) {
                configuracion.setValor(item.getValor());
            }
            configuracionSistemaRepository.save(configuracion);
        }
    }

    private void actualizarPermisos(List<ConfiguracionAdminRequestDTO.PermisoPorUsuarioDTO> usuarios) {
        for (ConfiguracionAdminRequestDTO.PermisoPorUsuarioDTO usuarioDTO : usuarios) {
            String nombreTipoUsuario = usuarioDTO.getTipoUsuario().trim();
            TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombreTipoUsuarioIgnoreCase(nombreTipoUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado: " + nombreTipoUsuario));

            for (ConfiguracionAdminRequestDTO.PermisoEstadoDTO permisoDTO : usuarioDTO.getPermisos()) {
                String nombrePermiso = permisoDTO.getNombre().trim();
                Permiso permiso = permisoRepository.findByNombreConfiguracionIgnoreCase(nombrePermiso)
                    .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado: " + nombrePermiso));

                PermisoTipoUsuario permisoTipoUsuario = permisoTipoUsuarioRepository
                    .findByTipoUsuarioAndPermiso(tipoUsuario, permiso)
                    .orElseGet(() -> {
                        PermisoTipoUsuario nuevo = new PermisoTipoUsuario();
                        nuevo.setTipoUsuario(tipoUsuario);
                        nuevo.setPermiso(permiso);
                        nuevo.setActivo(false);
                        return nuevo;
                    });

                permisoTipoUsuario.setActivo(Boolean.TRUE.equals(permisoDTO.getActivo()));
                permisoTipoUsuarioRepository.save(permisoTipoUsuario);
            }
        }
    }
}
