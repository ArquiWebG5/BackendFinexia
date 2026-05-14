package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Meta de ahorro del usuario. Soporta HU 14 - HU 18.
@Entity
@Table(name = "meta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "monto_objetivo", nullable = false)
    private double montoObjetivo;

    @Column(name = "balance_inicial")
    private double balanceInicial;

    @Column(name = "contribucion_mensual")
    private double contribucionMensual;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    // HU 17 - Visualizar progreso de meta.
    @Column(name = "progreso_actual")
    private double progresoActual;

    @Column(length = 50)
    private String categoria;

    @Column(length = 20)
    private String estado;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
