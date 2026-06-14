package com.upc.finexia.repositories.projections;

public interface ReporteAnalisisAhorroProjection {
    String getMes();
    Number getTotalIngresos();
    Number getTotalEgresos();
    Number getAhorroReal();
    Number getTasaAhorroPct();
}
