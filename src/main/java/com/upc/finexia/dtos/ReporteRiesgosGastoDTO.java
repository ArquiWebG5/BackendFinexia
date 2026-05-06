package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteRiesgosGastoDTO {
    private String categoria;
    private Double gastoActual;
    private Double promedioHistorico;
    private Double variacionPorcentual;
    private String nivelRiesgo; // "RIESGO_ALTO", "RIESGO_MEDIO", "NORMAL", "SIN_HISTORIAL"
}