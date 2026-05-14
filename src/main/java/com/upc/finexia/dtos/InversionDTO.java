package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de Inversion (HU 21 - registrar, HU 22 - editar, HU 24 - listar, HU 25 - portafolio).
// Usa wrappers Double para tolerar valores nulos al mapear desde la entidad.
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
    private Double precioCompra;
    private Double cantidad;
    private LocalDate fechaCompra;
    private double valorTotal;
    private String categoria;
    private LocalDate creadoEn;
}
