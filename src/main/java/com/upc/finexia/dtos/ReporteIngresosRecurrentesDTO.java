package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteIngresosRecurrentesDTO {
    private String categoria;
    private Long mesesPresentes;
    private Double montoPromedio;
    private Double montoMinimo;
    private Double montoMaximo;
}