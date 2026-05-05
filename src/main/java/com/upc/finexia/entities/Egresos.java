package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "egresos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class Egresos {

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

    @Column(length = 20)
    private String categoria;

    @Column(length = 250)
    private String nota;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
