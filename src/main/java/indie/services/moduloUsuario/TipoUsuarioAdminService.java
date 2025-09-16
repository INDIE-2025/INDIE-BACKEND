package indie.services.moduloUsuario;

import indie.dtos.moduloUsuario.admin.TipoUsuarioAdminRequestDTO;
import indie.dtos.moduloUsuario.admin.TipoUsuarioAdminResponseDTO;
import indie.models.moduloUsuario.SubTipoUsuario;
import indie.models.moduloUsuario.TipoUsuario;
import indie.repositories.moduloUsuario.SubTipoUsuarioRepository;
import indie.repositories.moduloUsuario.TipoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TipoUsuarioAdminService {

    private static final DateTimeFormatter FECHA_ALTA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SubTipoUsuarioRepository subTipoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<TipoUsuarioAdminResponseDTO> obtenerTodos() {
        return tipoUsuarioRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public TipoUsuarioAdminResponseDTO obtenerPorNombre(String nombreTipoUsuario) {
        String nombreNormalizado = normalizarNombreTipoDesdeRuta(nombreTipoUsuario);
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombreTipoUsuarioIgnoreCase(nombreNormalizado)
            .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado"));
        return toResponse(tipoUsuario);
    }

    @Transactional
    public TipoUsuarioAdminResponseDTO crear(TipoUsuarioAdminRequestDTO dto) {
        String nombreTipo = dto.getNombreTipoUsuario().trim();
        if (tipoUsuarioRepository.existsByNombreTipoUsuarioIgnoreCase(nombreTipo)) {
            throw new IllegalArgumentException("Ya existe un tipo de usuario con ese nombre");
        }

        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setNombreTipoUsuario(nombreTipo);
        tipoUsuario.setDeletedAt(resolveDeletedAtFromEstado(dto.getEstado()));
        TipoUsuario guardado = tipoUsuarioRepository.save(tipoUsuario);

        List<SubTipoUsuario> subTipos = new ArrayList<>();
        for (String nombreSubTipo : dto.getSubtipos()) {
            SubTipoUsuario subTipo = new SubTipoUsuario();
            subTipo.setNombreSubTipoUsuario(nombreSubTipo.trim());
            subTipo.setTipoUsuario(guardado);
            subTipo.setDeletedAt(guardado.getDeletedAt());
            subTipos.add(subTipo);
        }
        subTipoUsuarioRepository.saveAll(subTipos);
        return toResponse(guardado);
    }

    @Transactional
    public TipoUsuarioAdminResponseDTO actualizar(String nombreTipoUsuario, TipoUsuarioAdminRequestDTO dto) {
        String nombrePath = normalizarNombreTipoDesdeRuta(nombreTipoUsuario);
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombreTipoUsuarioIgnoreCase(nombrePath)
            .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado"));

        String nombreNuevo = dto.getNombreTipoUsuario().trim();
        if (!tipoUsuario.getNombreTipoUsuario().equalsIgnoreCase(nombreNuevo)
            && tipoUsuarioRepository.existsByNombreTipoUsuarioIgnoreCase(nombreNuevo)) {
            throw new IllegalArgumentException("Ya existe un tipo de usuario con ese nombre");
        }

        tipoUsuario.setNombreTipoUsuario(nombreNuevo);
        tipoUsuario.setDeletedAt(resolveDeletedAtFromEstado(dto.getEstado()));
        TipoUsuario actualizado = tipoUsuarioRepository.save(tipoUsuario);

        actualizarSubtipos(actualizado, dto);
        return toResponse(actualizado);
    }

    private void actualizarSubtipos(TipoUsuario tipoUsuario, TipoUsuarioAdminRequestDTO dto) {
        List<SubTipoUsuario> existentes = subTipoUsuarioRepository.findByTipoUsuario_Id(tipoUsuario.getId());
        Map<String, String> nuevosNormalizados = dto.getSubtipos().stream()
            .map(nombre -> nombre != null ? nombre.trim() : null)
            .filter(nombre -> nombre != null && !nombre.isEmpty())
            .collect(Collectors.toMap(
                this::normalizarNombreSubTipo,
                Function.identity(),
                (valorExistente, valorIgnorado) -> valorExistente,
                LinkedHashMap::new));

        LocalDateTime marcaBaja = tipoUsuario.getDeletedAt() != null ? tipoUsuario.getDeletedAt() : LocalDateTime.now();

        for (SubTipoUsuario existente : existentes) {
            String claveNormalizada = normalizarNombreSubTipo(existente.getNombreSubTipoUsuario());
            if (nuevosNormalizados.containsKey(claveNormalizada)) {
                existente.setNombreSubTipoUsuario(nuevosNormalizados.get(claveNormalizada));
                existente.setDeletedAt(tipoUsuario.getDeletedAt());
                subTipoUsuarioRepository.save(existente);
                nuevosNormalizados.remove(claveNormalizada);
            } else {
                existente.setDeletedAt(marcaBaja);
                subTipoUsuarioRepository.save(existente);
            }
        }

        for (String nombreNormalizado : nuevosNormalizados.keySet()) {
            SubTipoUsuario nuevo = new SubTipoUsuario();
            nuevo.setNombreSubTipoUsuario(nuevosNormalizados.get(nombreNormalizado));
            nuevo.setTipoUsuario(tipoUsuario);
            nuevo.setDeletedAt(tipoUsuario.getDeletedAt());
            subTipoUsuarioRepository.save(nuevo);
        }
    }

    private String normalizarNombreSubTipo(String nombre) {
        return nombre == null ? "" : nombre.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizarNombreTipoDesdeRuta(String rawNombre) {
        if (rawNombre == null) {
            throw new IllegalArgumentException("El nombre del tipo de usuario es obligatorio");
        }
        String valor = rawNombre.trim();
        int indiceSeparador = valor.indexOf(':');
        if (indiceSeparador > -1) {
            String prefijo = valor.substring(0, indiceSeparador).toLowerCase(Locale.ROOT);
            if (prefijo.contains("tipo") || prefijo.contains("nombre")) {
                valor = valor.substring(indiceSeparador + 1).trim();
            }
        }
        return valor;
    }

    private TipoUsuarioAdminResponseDTO toResponse(TipoUsuario tipoUsuario) {
        List<String> subtiposActivos = subTipoUsuarioRepository.findByTipoUsuario_IdAndDeletedAtIsNull(tipoUsuario.getId()).stream()
            .map(SubTipoUsuario::getNombreSubTipoUsuario)
            .collect(Collectors.toList());

        long cantidadUsuarios = usuarioRepository.countBySubTipoUsuario_TipoUsuario_IdAndDeletedAtIsNull(tipoUsuario.getId());
        String fechaFormateada = tipoUsuario.getCreatedAt() != null
            ? tipoUsuario.getCreatedAt().format(FECHA_ALTA_FORMATTER)
            : null;

        return TipoUsuarioAdminResponseDTO.builder()
            .nombreTipoUsuario(tipoUsuario.getNombreTipoUsuario())
            .subtipos(subtiposActivos)
            .cantidadUsuarios(cantidadUsuarios)
            .fechaAlta(fechaFormateada)
            .estado(tipoUsuario.getDeletedAt() == null ? "Activo" : "De baja")
            .build();
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
}

