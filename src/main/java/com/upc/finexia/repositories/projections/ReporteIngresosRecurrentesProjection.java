package com.upc.finexia.repositories.projections;

public interface ReporteIngresosRecurrentesProjection {
    String getCategoria();
    Number getMesesPresentes();
    Number getMontoPromedio();
    Number getMontoMinimo();
    Number getMontoMaximo();
}
