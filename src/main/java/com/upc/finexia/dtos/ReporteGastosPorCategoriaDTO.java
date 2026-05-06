package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteGastosPorCategoriaDTO {
    private String categoria;
    private Long cantidad;
    private Double total;
    private Double promedio;
    private Double maximo;
    private Double minimo;
}
