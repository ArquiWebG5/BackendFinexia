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
public class MetaDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private double montoObjetivo;
    private double balanceInicial;
    private double contribucionMensual;
    private LocalDate fechaObjetivo;
    private double progresoActual;
    private String categoria;
    private String estado;
    private LocalDate creadoEn;
}