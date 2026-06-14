package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Venta historica de una posicion de inversion. Soporta HU 10 - Registrar venta de activo.
@Entity
@Table(name = "venta_activo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaActivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inversion_id", nullable = false)
    private Inversion inversion;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @Column(name = "nombre_activo", nullable = false, length = 200)
    private String nombreActivo;

    @Column(length = 20)
    private String ticker;

    @Column(name = "cantidad_vendida", nullable = false)
    private Double cantidadVendida;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "costo_promedio")
    private Double costoPromedio;

    @Column(name = "ganancia_perdida")
    private Double gananciaPerdida;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Column(length = 250)
    private String nota;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creadoEn;
}
