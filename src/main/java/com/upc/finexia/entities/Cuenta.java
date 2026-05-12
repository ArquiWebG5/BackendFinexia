package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cuenta")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long idCuenta;  // ✅ coincide con BD

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)  // ✅ coincide con BD
    private Usuario usuario;

    @Column(name = "nombre_cuenta", nullable = false, length = 100)
    private String nombreCuenta;

    @Column(name = "banco_nombre", nullable = false, length = 100)
    private String bancoNombre;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @Column(length = 10)
    private String moneda;

    @Column(name = "saldo_inicial")
    private double saldoInicial;

    @Column(name = "saldo_actual")
    private double saldoActual;

    private Boolean activa;

    @Column(name = "creado_en")
    private LocalDate creadoEn;
}