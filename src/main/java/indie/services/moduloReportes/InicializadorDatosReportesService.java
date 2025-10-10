package indie.services.moduloReportes;

import indie.models.moduloReportes.MetricaUsuario;
import indie.models.moduloReportes.TipoMetrica;
import indie.models.moduloReportes.VisualizacionPerfil;
import indie.models.moduloUsuario.Usuario;
import indie.repositories.moduloReportes.MetricaUsuarioRepository;
import indie.repositories.moduloReportes.TipoMetricaRepository;
import indie.repositories.moduloReportes.VisualizacionPerfilRepository;
import indie.repositories.moduloUsuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class InicializadorDatosReportesService implements ApplicationRunner {
    
    private final MetricaUsuarioRepository metricaUsuarioRepository;
    private final VisualizacionPerfilRepository visualizacionPerfilRepository;
    private final TipoMetricaRepository tipoMetricaRepository;
    private final UsuarioRepository usuarioRepository;
    
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Solo ejecutar si no hay datos de métricas
        if (metricaUsuarioRepository.count() == 0) {
            log.info("Inicializando datos de ejemplo para reportes...");
            generarDatosEjemplo();
        }
    }
    
    private void generarDatosEjemplo() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            log.warn("No hay usuarios en el sistema para generar datos de ejemplo");
            return;
        }
        
        List<TipoMetrica> tiposMetrica = tipoMetricaRepository.findAll();
        if (tiposMetrica.isEmpty()) {
            log.warn("No hay tipos de métrica configurados");
            return;
        }
        
        Random random = new Random();
        
        // Generar datos para los últimos 3 meses
        LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(3);
        LocalDateTime fechaFin = LocalDateTime.now();
        
        for (Usuario usuario : usuarios.subList(0, Math.min(usuarios.size(), 5))) { // Solo primeros 5 usuarios
            generarMetricasParaUsuario(usuario, tiposMetrica, fechaInicio, fechaFin, random);
            generarVisualizacionesParaUsuario(usuario, fechaInicio, fechaFin, random);
        }
        
        log.info("Datos de ejemplo generados exitosamente");
    }
    
    private void generarMetricasParaUsuario(Usuario usuario, List<TipoMetrica> tiposMetrica, 
                                          LocalDateTime fechaInicio, LocalDateTime fechaFin, Random random) {
        
        TipoMetrica visualizaciones = tiposMetrica.stream()
                .filter(tm -> "VISUALIZACIONES_PERFIL".equals(tm.getNombre()))
                .findFirst().orElse(null);
        
        TipoMetrica usuariosInteresados = tiposMetrica.stream()
                .filter(tm -> "USUARIOS_INTERESADOS".equals(tm.getNombre()))
                .findFirst().orElse(null);
        
        TipoMetrica seguidoresNuevos = tiposMetrica.stream()
                .filter(tm -> "SEGUIDORES_NUEVOS".equals(tm.getNombre()))
                .findFirst().orElse(null);
        
        // Generar métricas diarias para los últimos 30 días
        LocalDateTime fecha = LocalDateTime.now().minusDays(30);
        while (!fecha.isAfter(LocalDateTime.now())) {
            
            // Visualizaciones del perfil (0-20 por día)
            if (visualizaciones != null) {
                MetricaUsuario metrica = MetricaUsuario.builder()
                        .usuario(usuario)
                        .tipoMetrica(visualizaciones)
                        .valor(new BigDecimal(random.nextInt(21)))
                        .fechaMetrica(fecha)
                        .periodoMes(fecha.getMonthValue())
                        .periodoAnio(fecha.getYear())
                        .build();
                metricaUsuarioRepository.save(metrica);
            }
            
            // Usuarios interesados (0-5 por día)
            if (usuariosInteresados != null && random.nextDouble() > 0.7) { // 30% de probabilidad
                MetricaUsuario metrica = MetricaUsuario.builder()
                        .usuario(usuario)
                        .tipoMetrica(usuariosInteresados)
                        .valor(new BigDecimal(random.nextInt(6)))
                        .fechaMetrica(fecha)
                        .periodoMes(fecha.getMonthValue())
                        .periodoAnio(fecha.getYear())
                        .build();
                metricaUsuarioRepository.save(metrica);
            }
            
            // Seguidores nuevos (0-3 por día)
            if (seguidoresNuevos != null && random.nextDouble() > 0.8) { // 20% de probabilidad
                MetricaUsuario metrica = MetricaUsuario.builder()
                        .usuario(usuario)
                        .tipoMetrica(seguidoresNuevos)
                        .valor(new BigDecimal(random.nextInt(4)))
                        .fechaMetrica(fecha)
                        .periodoMes(fecha.getMonthValue())
                        .periodoAnio(fecha.getYear())
                        .build();
                metricaUsuarioRepository.save(metrica);
            }
            
            fecha = fecha.plusDays(1);
        }
    }
    
    private void generarVisualizacionesParaUsuario(Usuario usuario, LocalDateTime fechaInicio, 
                                                 LocalDateTime fechaFin, Random random) {
        
        List<Usuario> otrosUsuarios = usuarioRepository.findAll().stream()
                .filter(u -> !u.getId().equals(usuario.getId()))
                .toList();
        
        if (otrosUsuarios.isEmpty()) return;
        
        // Generar visualizaciones para los últimos 30 días
        LocalDateTime fecha = LocalDateTime.now().minusDays(30);
        while (!fecha.isAfter(LocalDateTime.now())) {
            
            // 0-5 visualizaciones por día
            int visualizacionesDelDia = random.nextInt(6);
            for (int i = 0; i < visualizacionesDelDia; i++) {
                Usuario visitante = random.nextBoolean() && !otrosUsuarios.isEmpty() ? 
                        otrosUsuarios.get(random.nextInt(otrosUsuarios.size())) : null;
                
                VisualizacionPerfil visualizacion = VisualizacionPerfil.builder()
                        .usuarioVisitado(usuario)
                        .usuarioVisitante(visitante)
                        .fechaVisualizacion(fecha.plusHours(random.nextInt(24)).plusMinutes(random.nextInt(60)))
                        .direccionIp("192.168.1." + random.nextInt(255))
                        .userAgent("Mozilla/5.0 (Example Browser)")
                        .build();
                visualizacionPerfilRepository.save(visualizacion);
            }
            
            fecha = fecha.plusDays(1);
        }
    }
}