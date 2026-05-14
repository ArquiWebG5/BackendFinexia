package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de Cuenta bancaria. Apoya HU 30 (dashboard) y reportes financieros del hogar.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaDTO {
    private Long id;
    private Long usuarioId;
    private String nombreCuenta;
    private String bancoNombre;
    private String tipoCuenta;
    private String moneda;
    private double saldoInicial;
    private double saldoActual;
    private Boolean activa;
    private LocalDate creadoEn;
}