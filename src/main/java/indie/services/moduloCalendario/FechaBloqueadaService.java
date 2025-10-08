package indie.services.moduloCalendario;

import indie.dtos.moduloCalendario.ActualizarFechaBloqueadaDTO;
import indie.dtos.moduloCalendario.BloquearFechasDTO;
import indie.dtos.moduloCalendario.FechaBloqueadaResponseDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FechaBloqueadaService {
    
    /**
     * Bloquear un rango de fechas en un calendario
     */
    FechaBloqueadaResponseDTO bloquearFechas(BloquearFechasDTO dto);
    
    /**
     * Obtener todas las fechas bloqueadas activas de un calendario
     */
    List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadas(String calendarioId);
    
    /**
     * Obtener fechas bloqueadas en un rango específico
     */
    List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadasEnRango(
            String calendarioId, LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Verificar si una fecha está bloqueada
     */
    boolean isFechaBloqueada(String calendarioId, LocalDate fecha);
    
    /**
     * Verificar si una fecha y hora específica está bloqueada
     */
    boolean isFechaHoraBloqueada(String calendarioId, LocalDate fecha, LocalTime hora);
    
    /**
     * Obtener una fecha bloqueada por su ID
     */
    FechaBloqueadaResponseDTO obtenerFechaBloqueadaPorId(String id);
    
    /**
     * Actualizar una fecha bloqueada
     */
    FechaBloqueadaResponseDTO actualizarFechaBloqueada(String id, ActualizarFechaBloqueadaDTO dto);
    
    /**
     * Eliminar (desactivar) una fecha bloqueada
     */
    void eliminarFechaBloqueada(String id);
    
    /**
     * Buscar fechas bloqueadas por motivo
     */
    List<FechaBloqueadaResponseDTO> buscarPorMotivo(String calendarioId, String motivo);
    
    /**
     * Obtener fechas bloqueadas futuras
     */
    List<FechaBloqueadaResponseDTO> obtenerFechasBloqueadasFuturas(String calendarioId);
    
    /**
     * Validar si hay solapamientos con fechas ya bloqueadas
     */
    boolean validarSolapamiento(String calendarioId, LocalDate fechaInicio, LocalDate fechaFin, String excludeId);
}