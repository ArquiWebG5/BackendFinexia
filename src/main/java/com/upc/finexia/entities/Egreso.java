package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Egreso/gasto registrado contra una Cuenta. Soporta HU 06, HU 09, HU 11, HU 13.
@Entity
@Table(name = "egreso")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 500)
    private String comprobante;

    // HU 13 - Clasificar gastos por categoria.
    @Column(length = 20)
    private String categoria;

    @Column(length = 250)
    private String nota;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
