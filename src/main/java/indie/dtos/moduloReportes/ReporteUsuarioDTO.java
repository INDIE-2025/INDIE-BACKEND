package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteUsuarioDTO {
    private String usuarioId;
    private String nombreUsuario;
    private String username;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoAgrupacion; // "DIA" o "MES"
    private List<MetricaDTO> visualizacionesPerfil;
    private List<MetricaDTO> usuariosInteresados;
    private List<EventoPopularDTO> eventosMasPopulares;
    private List<MetricaDTO> seguidoresNuevos;
    private ResumenMetricasDTO resumen;
}