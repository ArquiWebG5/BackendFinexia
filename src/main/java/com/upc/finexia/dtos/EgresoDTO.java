package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EgresoDTO {
    private Long id;
    private Long cuentaId;
    private double monto;
    private LocalDate fecha;
    private String comprobante;
    private String categoria;
    private String nota;
    private LocalDate creadoEn;
}