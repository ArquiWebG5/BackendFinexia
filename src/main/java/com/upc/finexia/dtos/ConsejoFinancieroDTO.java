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
public class ConsejoFinancieroDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private String categoria; // "Ahorro", "Inversión", "Deuda", "Presupuesto"
    private String fuente;
    private LocalDate fechaPublicacion;
    private Integer lecturas;
    private Double calificacion; // 1-5
}