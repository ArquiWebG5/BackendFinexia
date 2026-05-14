package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO de snapshot de Portafolio (HU 25 - Visualizar portafolio).
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PortafolioDTO {
    private Long id;
    private Long usuarioId;
    private double valorTotal;
    private double rendimientoHistorico;
    private String distribucionActivos;
    private String nivelDiversificacion;
}
