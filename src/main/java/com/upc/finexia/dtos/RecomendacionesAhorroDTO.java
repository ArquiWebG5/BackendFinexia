package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecomendacionesAhorroDTO {
    private String categoria;
    private Double gastoActual;
    private Double gastoPromedio;
    private Double potencialAhorro;
    private String recomendacion;
    private Integer prioridad; // 1 (Alta), 2 (Media), 3 (Baja)
}