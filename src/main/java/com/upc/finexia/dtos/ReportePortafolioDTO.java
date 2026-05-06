package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportePortafolioDTO {
    private String tipoActivo;
    private String categoria;
    private Long cantidadActivos;
    private Double valorTotal;
    private Double cantidadTotal;
    private Double precioPromedioCompra;
    private LocalDate primeraCompra;
    private LocalDate ultimaCompra;
}