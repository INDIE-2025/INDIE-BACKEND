package indie.dtos.moduloReportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoPopularDTO {
    private String eventoId;
    private String tituloEvento;
    private String descripcionEvento;
    private BigDecimal cantidadInteresados;
    private String fechaEvento;
    private String ubicacionEvento;
    private Boolean esColaborador; // Si el usuario es colaborador del evento
}