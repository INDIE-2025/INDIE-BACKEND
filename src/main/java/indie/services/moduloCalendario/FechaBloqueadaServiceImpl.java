    package indie.services.moduloCalendario;

    import indie.dtos.moduloCalendario.ActualizarFechaBloqueadaDTO;
    import indie.dtos.moduloCalendario.BloquearFechasDTO;
    import indie.dtos.moduloCalendario.FechaBloqueadaResponseDTO;
    import indie.models.moduloCalendario.Calendario;
    import indie.models.moduloCalendario.FechaCalendario;
    import indie.repositories.moduloCalendario.CalendarioRepository;
    import indie.repositories.moduloCalendario.FechaCalendarioRepository;

    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @Transactional
    public class FechaBloqueadaServiceImpl implements FechaBloqueadaService {
        
        private final FechaCalendarioRepository fechaCalendarioRepository;
        private final CalendarioRepository calendarioRepository;
        
        public FechaBloqueadaServiceImpl(FechaCalendarioRepository fechaCalendarioRepository,
                                    CalendarioRepository calendarioRepository) {
            this.fechaCalendarioRepository = fechaCalendarioRepository;
            this.calendarioRepository = calendarioRepository;
        }
        
        @Override
        public FechaBloqueadaResponseDTO bloquearFechas(BloquearFechasDTO dto) {
            // Validar que el calendario existe
            Calendario calendario = calendarioRepository.findById(dto.getCalendarioId())
                    .orElseThrow(() -> new RuntimeException("Calendario no encontrado"));
            
            // Validar fechas
            validarFechas(dto.getFechaInicio(), dto.getFechaFin());
            
            // Validar que no hay solapamientos
            if (validarSolapamiento(dto.getCalendarioId(), dto.getFechaInicio(), dto.getFechaFin(), null)) {
                throw new RuntimeException("Las fechas se solapan con un bloqueo existente");
            }
            
            // Crear el bloqueo usando FechaCalendario
            // CLAVE: idEvento = null indica que es un bloqueo, deletedAt = null indica que está activo
            FechaCalendario fechaBloqueo = FechaCalendario.builder()
                    .idCalendario(calendario)
                    .idEvento(null)  // NULL = Es un bloqueo
                    .fechaDesde(dto.getFechaInicio())
                    .fechaHasta(dto.getFechaFin())
                    .horaDesde(dto.getHoraInicio())
                    .horaHasta(dto.getHoraFin())
                    .motivo(dto.getMotivo())
                    .todoElDia(dto.getHoraInicio() == null && dto.getHoraFin() == null)
                    // deletedAt será null por defecto (activo)
                    .build();
            
            FechaCalendario fechaGuardada = fechaCalendarioRepository.save(fechaBloqueo);
            return convertirAResponseDTO(fechaGuardada);
        }
        
        @Override
        @Transactional(readOnly = true)
        public List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadas(String calendarioId) {
            List<FechaCalendario> fechas = fechaCalendarioRepository.findBloqueosByCalendario(calendarioId);
            return fechas.stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
        }
        
        @Override
        @Transactional(readOnly = true)
        public List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadasEnRango(
                String calendarioId, LocalDate fechaInicio, LocalDate fechaFin) {
            List<FechaCalendario> fechas = fechaCalendarioRepository
                    .findBloqueosEnRango(calendarioId, fechaInicio, fechaFin);
            return fechas.stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
        }
        
        @Override
        @Transactional(readOnly = true)
        public boolean isFechaBloqueada(String calendarioId, LocalDate fecha) {
            return fechaCalendarioRepository.isFechaBloqueada(calendarioId, fecha);
        }
        
        @Override
        @Transactional(readOnly = true)
        public boolean isFechaHoraBloqueada(String calendarioId, LocalDate fecha, LocalTime hora) {
            return fechaCalendarioRepository.isFechaHoraBloqueada(calendarioId, fecha, hora);
        }
        
        @Override
        @Transactional(readOnly = true)
        public FechaBloqueadaResponseDTO obtenerFechaBloqueadaPorId(String id) {
            FechaCalendario fechaCalendario = fechaCalendarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fecha bloqueada no encontrada"));
            
            if (!fechaCalendario.esBloqueo()) {
                throw new RuntimeException("La fecha especificada no es un bloqueo");
            }
            
            return convertirAResponseDTO(fechaCalendario);
        }
        
        @Override
        public FechaBloqueadaResponseDTO actualizarFechaBloqueada(String id, ActualizarFechaBloqueadaDTO dto) {
            FechaCalendario fechaCalendario = fechaCalendarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fecha bloqueada no encontrada"));
            
            if (!fechaCalendario.esBloqueo()) {
                throw new RuntimeException("La fecha especificada no es un bloqueo");
            }
            
            // Validar fechas si se proporcionan
            if (dto.getFechaInicio() != null && dto.getFechaFin() != null) {
                validarFechas(dto.getFechaInicio(), dto.getFechaFin());
                
                // Validar solapamientos excluyendo el registro actual
                if (validarSolapamiento(fechaCalendario.getIdCalendario().getId(), 
                                    dto.getFechaInicio(), dto.getFechaFin(), id)) {
                    throw new RuntimeException("Las fechas se solapan con un bloqueo existente");
                }
                
                fechaCalendario.setFechaDesde(dto.getFechaInicio());
                fechaCalendario.setFechaHasta(dto.getFechaFin());
            }
            
            if (dto.getHoraInicio() != null) {
                fechaCalendario.setHoraDesde(dto.getHoraInicio());
            }
            if (dto.getHoraFin() != null) {
                fechaCalendario.setHoraHasta(dto.getHoraFin());
            }
            if (dto.getMotivo() != null) {
                fechaCalendario.setMotivo(dto.getMotivo());
            }
            
            // Actualizar todoElDia basado en las horas
            if (dto.getHoraInicio() != null || dto.getHoraFin() != null) {
                fechaCalendario.setTodoElDia(dto.getHoraInicio() == null && dto.getHoraFin() == null);
            }
            
            FechaCalendario fechaActualizada = fechaCalendarioRepository.save(fechaCalendario);
            return convertirAResponseDTO(fechaActualizada);
        }
        
        @Override
        public void eliminarFechaBloqueada(String id) {
            FechaCalendario fechaCalendario = fechaCalendarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fecha bloqueada no encontrada"));
            
            if (!fechaCalendario.esBloqueo()) {
                throw new RuntimeException("La fecha especificada no es un bloqueo");
            }
            
            // DESBLOQUEAR: Usar soft delete con deletedAt
            fechaCalendario.setDeletedAt(LocalDateTime.now());
            fechaCalendarioRepository.save(fechaCalendario);
        }
        
        @Override
        @Transactional(readOnly = true)
        public List<FechaBloqueadaResponseDTO> buscarPorMotivo(String calendarioId, String motivo) {
            List<FechaCalendario> fechas = fechaCalendarioRepository
                    .findBloqueosByMotivo(calendarioId, motivo);
            return fechas.stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
        }
        
        @Override
        @Transactional(readOnly = true)
        public List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadasFuturas(String calendarioId) {
            List<FechaCalendario> fechas = fechaCalendarioRepository
                    .findBloqueosFuturos(calendarioId, LocalDate.now());
            return fechas.stream()
                    .map(this::convertirAResponseDTO)
                    .collect(Collectors.toList());
        }
        
        @Override
        @Transactional(readOnly = true)
        public boolean validarSolapamiento(String calendarioId, LocalDate fechaInicio, 
                                        LocalDate fechaFin, String excludeId) {
            List<FechaCalendario> solapamientos = fechaCalendarioRepository
                    .findSolapamientosBloqueo(calendarioId, fechaInicio, fechaFin, excludeId);
            return !solapamientos.isEmpty();
        }
        
        private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
            if (fechaInicio.isAfter(fechaFin)) {
                throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
            if (fechaInicio.isBefore(LocalDate.now())) {
                throw new RuntimeException("No se pueden bloquear fechas pasadas");
            }
        }
        
        private FechaBloqueadaResponseDTO convertirAResponseDTO(FechaCalendario fechaCalendario) {
            return FechaBloqueadaResponseDTO.builder()
                    .id(fechaCalendario.getId())
                    .calendarioId(fechaCalendario.getIdCalendario().getId())
                    .fechaInicio(fechaCalendario.getFechaDesde())
                    .fechaFin(fechaCalendario.getFechaHasta())
                    .horaInicio(fechaCalendario.getHoraDesde())
                    .horaFin(fechaCalendario.getHoraHasta())
                    .motivo(fechaCalendario.getMotivo())
                    .todoElDia(fechaCalendario.getTodoElDia())
                    .fechaCreacion(fechaCalendario.getCreatedAt())
                    .fechaModificacion(fechaCalendario.getUpdatedAt())
                    .activo(fechaCalendario.estaActivo())  // Usa deletedAt para determinar si está activo
                    .build();
        }
    }