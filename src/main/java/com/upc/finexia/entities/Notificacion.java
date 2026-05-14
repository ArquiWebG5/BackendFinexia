package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Notificacion enviada al usuario. Soporta HU 29 - Recibir notificaciones financieras.
@Entity
@Table(name = "notificacion")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 200)
    private String mensaje;

    @Column(length = 30)
    private String tipo;       // "ALERTA", "META", "INGRESO", etc.

    @Column(nullable = false)
    private Boolean leido;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
