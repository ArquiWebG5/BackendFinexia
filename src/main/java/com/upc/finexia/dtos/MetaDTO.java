package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de Meta de ahorro (HU 14 a HU 18).
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private double montoObjetivo;
    private double balanceInicial;
    private double contribucionMensual;
    private LocalDate fechaObjetivo;
    private double progresoActual;   // HU 17 - progreso de la meta
    private String categoria;
    private String estado;
    private LocalDate creadoEn;
}
