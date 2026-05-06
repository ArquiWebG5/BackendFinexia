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
public class ReporteTopGastosMesDTO {
    private Long id;
    private String categoria;
    private Double monto;
    private LocalDate fecha;
    private String nota;
    private String comprobante;
}