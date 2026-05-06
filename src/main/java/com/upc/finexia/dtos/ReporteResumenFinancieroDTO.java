package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteResumenFinancieroDTO {
    private Long cuentaId;
    private String nombreCuenta;
    private String bancoNombre;
    private String moneda;
    private Double totalIngresos;
    private Double totalEgresos;
    private Double balanceNeto;
    private Double saldoActual;
}