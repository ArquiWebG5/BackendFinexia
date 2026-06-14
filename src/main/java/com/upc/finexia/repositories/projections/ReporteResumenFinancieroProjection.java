package com.upc.finexia.repositories.projections;

public interface ReporteResumenFinancieroProjection {
    Number getCuentaId();
    String getNombreCuenta();
    String getBancoNombre();
    String getMoneda();
    Number getTotalIngresos();
    Number getTotalEgresos();
    Number getBalanceNeto();
    Number getSaldoActual();
}
