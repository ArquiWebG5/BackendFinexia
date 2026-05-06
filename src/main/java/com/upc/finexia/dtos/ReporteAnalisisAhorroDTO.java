package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteAnalisisAhorroDTO {
    private String mes;
    private Double totalIngresos;
    private Double totalEgresos;
    private Double ahorroReal;
    private Double tasaAhorroPct;
}