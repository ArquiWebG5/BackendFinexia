package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inversiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    // VERIFICA QUE ESTE NOMBRE ESTÉ BIEN ESCRITO:
    @Column(name = "nombre_activo", nullable = false, length = 200)
    private String nombreActivo;

    @Column(length = 20)
    private String ticker;

    @Column(name = "tipo_activo", length = 20)
    private String tipoActivo;

    @Column(length = 100)
    private String broker;

    @Column(name = "precio_compra")
    private Double precioCompra;

    private Double cantidad;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Column(name = "valor_total")
    private double valorTotal;

    @Column(length = 100)
    private String categoria;

    @Column(name = "creado_en")
    private LocalDate creadoEn;
}