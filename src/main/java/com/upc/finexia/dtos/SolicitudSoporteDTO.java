package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudSoporteDTO {
    private Long id;
    private Long usuarioId;
    private String asunto;
    private String descripcion;
    private String categoria; // "Bug", "Feature", "Rendimiento", "Otro"
    private String prioridad; // "Alta", "Media", "Baja"
    private String estado; // "Abierto", "En progreso", "Resuelto", "Cerrado"
    private String respuesta;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;
    private String asignadoA;
}