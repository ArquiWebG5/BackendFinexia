package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportePatrimonioNetoDTO {
    private Long usuarioId;
    private String monedaBase;
    private Double totalCuentas;
    private Double totalInversiones;
    private Double patrimonioNeto;
}
