package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "metas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Column(nullable = false, length = 150)
    private String nombre; // <-- ESTE NOMBRE GENERA EL MÉTODO setNombre()

    @Column(name = "monto_objetivo", nullable = false)
    private double montoObjetivo;

    @Column(name = "balance_inicial")
    private double balanceInicial;

    @Column(name = "contribucion_mensual")
    private double contribucionMensual;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    @Column(name = "progreso_actual")
    private double progresoActual;

    @Column(length = 50)
    private String categoria;

    @Column(length = 15)
    private String estado;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}