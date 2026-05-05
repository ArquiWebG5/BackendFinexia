package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InversionDTO {
    private Long id;
    private Long cuentaId;
    private String nombreActivo;
    private String ticker;
    private String tipoActivo;
    private String broker;
    private double precioCompra;
    private double cantidad;
    private LocalDate fechaCompra;
    private double valorTotal;
    private String categoria;
    private LocalDate creadoEn;
}