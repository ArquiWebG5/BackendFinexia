package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de Notificacion (HU 29 - Recibir notificaciones financieras).
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NotificacionDTO {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private String tipo;
    private Boolean leido;
    private LocalDate creadoEn;
}
