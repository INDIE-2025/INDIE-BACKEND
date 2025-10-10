package indie.services.moduloReportes;

import indie.dtos.moduloReportes.*;
import indie.models.moduloEventos.Evento;
import indie.models.moduloReportes.MetricaUsuario;
import indie.models.moduloReportes.TipoMetrica;
import indie.models.moduloReportes.VisualizacionPerfil;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloEventos.EventoRepository;
import indie.repositories.moduloEventos.InteresRepository;
import indie.repositories.moduloReportes.MetricaUsuarioRepository;
import indie.repositories.moduloReportes.TipoMetricaRepository;
import indie.repositories.moduloReportes.VisualizacionPerfilRepository;
import indie.repositories.moduloUsuario.SeguimientoUsuarioRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReporteUsuarioService {
    
    private final MetricaUsuarioRepository metricaUsuarioRepository;
    private final VisualizacionPerfilRepository visualizacionPerfilRepository;
    private final TipoMetricaRepository tipoMetricaRepository;
    private final EventoRepository eventoRepository;
    private final InteresRepository interesRepository;
    private final SeguimientoUsuarioRepository seguimientoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    
    // Constantes para tipos de métricas
    private static final String METRICA_VISUALIZACIONES = "VISUALIZACIONES_PERFIL";
    private static final String METRICA_USUARIOS_INTERESADOS = "USUARIOS_INTERESADOS";
    private static final String METRICA_SEGUIDORES_NUEVOS = "SEGUIDORES_NUEVOS";
    
    public ReporteUsuarioDTO generarReporteUsuario(String usuarioId, SolicitudReporteDTO solicitud) {
        return generarReporteUsuario(usuarioId, solicitud.getFechaInicio(), solicitud.getFechaFin());
    }
    
    public ReporteUsuarioDTO generarReporteUsuario(String usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Generando reporte para usuario {} desde {} hasta {}", 
                usuarioId, fechaInicio, fechaFin);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);
        
        // Determinar tipo de agrupación
        long diasDiferencia = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        String tipoAgrupacion = diasDiferencia < 30 ? "DIA" : "MES";
        
        ReporteUsuarioDTO reporte = ReporteUsuarioDTO.builder()
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .username(usuario.getUsername())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .tipoAgrupacion(tipoAgrupacion)
                .build();
        
        // Generar métricas
        reporte.setVisualizacionesPerfil(obtenerVisualizacionesPerfil(usuario, fechaInicioDateTime, fechaFinDateTime, tipoAgrupacion));
        reporte.setUsuariosInteresados(obtenerUsuariosInteresados(usuario, fechaInicioDateTime, fechaFinDateTime, tipoAgrupacion));
        reporte.setSeguidoresNuevos(obtenerSeguidoresNuevos(usuario, fechaInicioDateTime, fechaFinDateTime, tipoAgrupacion));
        reporte.setEventosMasPopulares(obtenerEventosMasPopulares(usuario, fechaInicioDateTime, fechaFinDateTime));
        reporte.setResumen(calcularResumen(reporte));
        
        return reporte;
    }
    
    private List<MetricaDTO> obtenerVisualizacionesPerfil(Usuario usuario, LocalDateTime fechaInicio, 
                                                          LocalDateTime fechaFin, String tipoAgrupacion) {
        List<MetricaDTO> metricas = new ArrayList<>();
        
        if ("DIA".equals(tipoAgrupacion)) {
            List<Object[]> resultados = visualizacionPerfilRepository.countVisualizacionesPorDia(usuario, fechaInicio, fechaFin);
            for (Object[] resultado : resultados) {
                LocalDate fecha = ((java.sql.Date) resultado[0]).toLocalDate();
                Long cantidad = (Long) resultado[1];
                
                metricas.add(MetricaDTO.builder()
                        .nombreMetrica("Visualizaciones del Perfil")
                        .valor(new BigDecimal(cantidad))
                        .unidadMedida("visualizaciones")
                        .fecha(fecha)
                        .fechaMetrica(fecha.atStartOfDay())
                        .build());
            }
        } else {
            List<Object[]> resultados = visualizacionPerfilRepository.countVisualizacionesPorMes(usuario, fechaInicio, fechaFin);
            for (Object[] resultado : resultados) {
                Integer mes = (Integer) resultado[0];
                Integer anio = (Integer) resultado[1];
                Long cantidad = (Long) resultado[2];
                
                metricas.add(MetricaDTO.builder()
                        .nombreMetrica("Visualizaciones del Perfil")
                        .valor(new BigDecimal(cantidad))
                        .unidadMedida("visualizaciones")
                        .mes(mes)
                        .anio(anio)
                        .fechaMetrica(LocalDate.of(anio, mes, 1).atStartOfDay())
                        .build());
            }
        }
        
        return metricas;
    }
    
    private List<MetricaDTO> obtenerUsuariosInteresados(Usuario usuario, LocalDateTime fechaInicio, 
                                                       LocalDateTime fechaFin, String tipoAgrupacion) {
        List<MetricaDTO> metricas = new ArrayList<>();
        
        // Obtener eventos del usuario en el período
        List<Evento> eventos = eventoRepository.findByIdUsuarioAndFechaHoraEventoBetween(usuario, fechaInicio, fechaFin);
        
        if ("DIA".equals(tipoAgrupacion)) {
            // Agrupar por día
            for (LocalDate fecha = fechaInicio.toLocalDate(); !fecha.isAfter(fechaFin.toLocalDate()); fecha = fecha.plusDays(1)) {
                LocalDateTime inicioDelDia = fecha.atStartOfDay();
                LocalDateTime finDelDia = fecha.atTime(23, 59, 59);
                
                long totalInteresados = eventos.stream()
                        .filter(evento -> !evento.getFechaHoraEvento().isBefore(inicioDelDia) && 
                                         !evento.getFechaHoraEvento().isAfter(finDelDia))
                        .mapToLong(evento -> interesRepository.countByEvento(evento))
                        .sum();
                
                if (totalInteresados > 0) {
                    metricas.add(MetricaDTO.builder()
                            .nombreMetrica("Usuarios Interesados")
                            .valor(new BigDecimal(totalInteresados))
                            .unidadMedida("usuarios")
                            .fecha(fecha)
                            .fechaMetrica(inicioDelDia)
                            .build());
                }
            }
        } else {
            // Agrupar por mes
            for (int anio = fechaInicio.getYear(); anio <= fechaFin.getYear(); anio++) {
                int mesInicio = (anio == fechaInicio.getYear()) ? fechaInicio.getMonthValue() : 1;
                int mesFin = (anio == fechaFin.getYear()) ? fechaFin.getMonthValue() : 12;
                
                for (int mes = mesInicio; mes <= mesFin; mes++) {
                    LocalDateTime inicioDelMes = LocalDate.of(anio, mes, 1).atStartOfDay();
                    LocalDateTime finDelMes = LocalDate.of(anio, mes, 1).plusMonths(1).minusDays(1).atTime(23, 59, 59);
                    
                    long totalInteresados = eventos.stream()
                            .filter(evento -> !evento.getFechaHoraEvento().isBefore(inicioDelMes) && 
                                             !evento.getFechaHoraEvento().isAfter(finDelMes))
                            .mapToLong(evento -> interesRepository.countByEvento(evento))
                            .sum();
                    
                    if (totalInteresados > 0) {
                        metricas.add(MetricaDTO.builder()
                                .nombreMetrica("Usuarios Interesados")
                                .valor(new BigDecimal(totalInteresados))
                                .unidadMedida("usuarios")
                                .mes(mes)
                                .anio(anio)
                                .fechaMetrica(inicioDelMes)
                                .build());
                    }
                }
            }
        }
        
        return metricas;
    }
    
    private List<MetricaDTO> obtenerSeguidoresNuevos(Usuario usuario, LocalDateTime fechaInicio, 
                                                    LocalDateTime fechaFin, String tipoAgrupacion) {
        List<MetricaDTO> metricas = new ArrayList<>();
        
        if ("DIA".equals(tipoAgrupacion)) {
            for (LocalDate fecha = fechaInicio.toLocalDate(); !fecha.isAfter(fechaFin.toLocalDate()); fecha = fecha.plusDays(1)) {
                LocalDateTime inicioDelDia = fecha.atStartOfDay();
                LocalDateTime finDelDia = fecha.atTime(23, 59, 59);
                
                long seguidoresNuevos = seguimientoUsuarioRepository.countByUsuarioSeguidoAndCreatedAtBetween(
                        usuario, inicioDelDia, finDelDia);
                
                if (seguidoresNuevos > 0) {
                    metricas.add(MetricaDTO.builder()
                            .nombreMetrica("Seguidores Nuevos")
                            .valor(new BigDecimal(seguidoresNuevos))
                            .unidadMedida("seguidores")
                            .fecha(fecha)
                            .fechaMetrica(inicioDelDia)
                            .build());
                }
            }
        } else {
            for (int anio = fechaInicio.getYear(); anio <= fechaFin.getYear(); anio++) {
                int mesInicio = (anio == fechaInicio.getYear()) ? fechaInicio.getMonthValue() : 1;
                int mesFin = (anio == fechaFin.getYear()) ? fechaFin.getMonthValue() : 12;
                
                for (int mes = mesInicio; mes <= mesFin; mes++) {
                    LocalDateTime inicioDelMes = LocalDate.of(anio, mes, 1).atStartOfDay();
                    LocalDateTime finDelMes = LocalDate.of(anio, mes, 1).plusMonths(1).minusDays(1).atTime(23, 59, 59);
                    
                    long seguidoresNuevos = seguimientoUsuarioRepository.countByUsuarioSeguidoAndCreatedAtBetween(
                            usuario, inicioDelMes, finDelMes);
                    
                    if (seguidoresNuevos > 0) {
                        metricas.add(MetricaDTO.builder()
                                .nombreMetrica("Seguidores Nuevos")
                                .valor(new BigDecimal(seguidoresNuevos))
                                .unidadMedida("seguidores")
                                .mes(mes)
                                .anio(anio)
                                .fechaMetrica(inicioDelMes)
                                .build());
                    }
                }
            }
        }
        
        return metricas;
    }
    
    private List<EventoPopularDTO> obtenerEventosMasPopulares(Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Evento> eventos = eventoRepository.findByIdUsuarioAndFechaHoraEventoBetween(usuario, fechaInicio, fechaFin);
        
        return eventos.stream()
                .map(evento -> {
                    long cantidadInteresados = interesRepository.countByEvento(evento);
                    return EventoPopularDTO.builder()
                            .eventoId(evento.getId())
                            .tituloEvento(evento.getTituloEvento())
                            .descripcionEvento(evento.getDescripcionEvento())
                            .cantidadInteresados(new BigDecimal(cantidadInteresados))
                            .fechaEvento(evento.getFechaHoraEvento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                            .ubicacionEvento(evento.getUbicacionEvento())
                            .esColaborador(false) // Por ahora, después se puede implementar la lógica de colaboradores
                            .build();
                })
                .sorted((e1, e2) -> e2.getCantidadInteresados().compareTo(e1.getCantidadInteresados()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    private ResumenMetricasDTO calcularResumen(ReporteUsuarioDTO reporte) {
        BigDecimal totalVisualizaciones = reporte.getVisualizacionesPerfil().stream()
                .map(MetricaDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalUsuariosInteresados = reporte.getUsuariosInteresados().stream()
                .map(MetricaDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSeguidoresNuevos = reporte.getSeguidoresNuevos().stream()
                .map(MetricaDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalEventos = reporte.getEventosMasPopulares().size();
        
        // Calcular promedios
        long diasDiferencia = ChronoUnit.DAYS.between(reporte.getFechaInicio(), reporte.getFechaFin()) + 1;
        BigDecimal promedioVisualizacionesDiarias = totalVisualizaciones.divide(
                new BigDecimal(diasDiferencia), 2, RoundingMode.HALF_UP);
        
        BigDecimal promedioInteresadosPorEvento = totalEventos > 0 ? 
                totalUsuariosInteresados.divide(new BigDecimal(totalEventos), 2, RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
        
        return ResumenMetricasDTO.builder()
                .totalVisualizaciones(totalVisualizaciones)
                .totalUsuariosInteresados(totalUsuariosInteresados)
                .totalSeguidoresNuevos(totalSeguidoresNuevos)
                .totalEventos(totalEventos)
                .promedioVisualizacionesDiarias(promedioVisualizacionesDiarias)
                .promedioInteresadosPorEvento(promedioInteresadosPorEvento)
                .build();
    }
    
    @Transactional
    public void registrarVisualizacionPerfil(String usuarioVisitadoId, String usuarioVisitanteId, String ip, String userAgent) {
        Usuario usuarioVisitado = usuarioRepository.findById(usuarioVisitadoId)
                .orElseThrow(() -> new RuntimeException("Usuario visitado no encontrado"));
        
        Usuario usuarioVisitante = null;
        if (usuarioVisitanteId != null) {
            usuarioVisitante = usuarioRepository.findById(usuarioVisitanteId).orElse(null);
        }
        
        VisualizacionPerfil visualizacion = VisualizacionPerfil.builder()
                .usuarioVisitado(usuarioVisitado)
                .usuarioVisitante(usuarioVisitante)
                .fechaVisualizacion(LocalDateTime.now())
                .direccionIp(ip)
                .userAgent(userAgent)
                .build();
        
        visualizacionPerfilRepository.save(visualizacion);
        log.info("Registrada visualización del perfil del usuario {} por {}", 
                usuarioVisitadoId, usuarioVisitanteId != null ? usuarioVisitanteId : "anónimo");
    }

    /**
     * Obtiene reportes detallados para exportación
     */
    public List<ReporteExportacionDTO> obtenerReportesDetallados(String usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo reportes detallados para usuario {} desde {} hasta {}", usuarioId, fechaInicio, fechaFin);
        
        List<ReporteExportacionDTO> reportes = new ArrayList<>();
        
        // Obtener métricas día por día
        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            LocalDate fechaSiguiente = fechaActual.plusDays(1);
            
            // Visualizaciones del perfil
            long visualizaciones = visualizacionPerfilRepository.contarVisualizacionesPorUsuarioYFecha(
                usuarioId, fechaActual.atStartOfDay(), fechaSiguiente.atStartOfDay());
            
            if (visualizaciones > 0) {
                reportes.add(ReporteExportacionDTO.builder()
                    .fecha(fechaActual)
                    .nombreMetrica("Visualizaciones del Perfil")
                    .valorMetrica(BigDecimal.valueOf(visualizaciones))
                    .unidadMedida("visualizaciones")
                    .descripcionMetrica("Número de veces que se visualizó el perfil")
                    .build());
            }
            
            // Usuarios interesados en eventos
            long usuariosInteresados = interesRepository.contarInteresesPorUsuarioYFecha(
                usuarioId, fechaActual.atStartOfDay(), fechaSiguiente.atStartOfDay());
            
            if (usuariosInteresados > 0) {
                reportes.add(ReporteExportacionDTO.builder()
                    .fecha(fechaActual)
                    .nombreMetrica("Usuarios Interesados")
                    .valorMetrica(BigDecimal.valueOf(usuariosInteresados))
                    .unidadMedida("usuarios")
                    .descripcionMetrica("Usuarios que mostraron interés en eventos")
                    .build());
            }
            
            // Seguidores nuevos
            long seguidoresNuevos = seguimientoUsuarioRepository.contarSeguidoresPorUsuarioYFecha(
                usuarioId, fechaActual.atStartOfDay(), fechaSiguiente.atStartOfDay());
            
            if (seguidoresNuevos > 0) {
                reportes.add(ReporteExportacionDTO.builder()
                    .fecha(fechaActual)
                    .nombreMetrica("Seguidores Nuevos")
                    .valorMetrica(BigDecimal.valueOf(seguidoresNuevos))
                    .unidadMedida("usuarios")
                    .descripcionMetrica("Nuevos seguidores adquiridos")
                    .build());
            }
            
            fechaActual = fechaActual.plusDays(1);
        }
        
        log.info("Se generaron {} registros de métricas para exportación", reportes.size());
        return reportes;
    }
}
