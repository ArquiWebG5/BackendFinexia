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
public class ReporteTopPosicionesPortafolioDTO {
    private String nombreActivo;
    private String ticker;
    private String tipoActivo;
    private Double cantidad;
    private Double precioCompra;
    private Double valorTotal;
    private String broker;
    private LocalDate fechaCompra;
}