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
public class VentaActivoDTO {
    private Long id;
    private Long inversionId;
    private Long cuentaId;
    private String nombreActivo;
    private String ticker;
    private Double cantidadVendida;
    private Double precioVenta;
    private Double montoTotal;
    private Double costoPromedio;
    private Double gananciaPerdida;
    private LocalDate fechaVenta;
    private String nota;
    private LocalDate creadoEn;
    private Double cantidadRestante;
    private Double valorRestante;
}
