package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de Egreso/gasto (HU 06, HU 09, HU 11, HU 13).
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EgresoDTO {
    private Long id;
    private Long cuentaId;
    private double monto;
    private LocalDate fecha;
    private String comprobante;
    private String categoria;   // HU 13 - clasificar gastos por categoria
    private String nota;
    private LocalDate creadoEn;
}