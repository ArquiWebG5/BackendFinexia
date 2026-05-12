package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ingreso")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Ingreso {

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

    @Column(length = 20)
    private String categoria;

    @Column(length = 250)
    private String nota;

    @Column(length = 500)
    private String comprobante;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
