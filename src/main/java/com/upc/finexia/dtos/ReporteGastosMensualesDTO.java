package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteGastosMensualesDTO {
    private String mes;
    private Double totalEgresos;
    private Long cantidadTransacciones;
}
