package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivoMercadoDTO {
    private String symbol;
    private String displaySymbol;
    private String descripcion;
    private String tipo;
    private Double precioActual;
    private Double variacion;
    private Double variacionPorcentual;
    private Double precioMaximoDia;
    private Double precioMinimoDia;
    private Double precioApertura;
    private Double precioCierrePrevio;
    private Long timestamp;
}
