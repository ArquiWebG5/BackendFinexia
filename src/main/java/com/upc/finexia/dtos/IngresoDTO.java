package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class IngresoDTO {
    private Long id;
    private Long cuentaId;
    private double monto;
    private LocalDate fecha;
    private String categoria;
    private String nota;
    private String comprobante;
    private LocalDate creadoEn;
}