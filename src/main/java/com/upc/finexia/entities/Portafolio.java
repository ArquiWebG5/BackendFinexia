package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "portafolio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "valor_total")
    private double valorTotal;

    @Column(name = "rendimiento_historico")
    private double rendimientoHistorico;

    @Column(name = "distribucion_activos", length = 250)
    private String distribucionActivos;   // JSON string con % por tipo

    @Column(name = "nivel_diversificacion", length = 50)
    private String nivelDiversificacion;  // "BAJO", "MEDIO", "ALTO"

}