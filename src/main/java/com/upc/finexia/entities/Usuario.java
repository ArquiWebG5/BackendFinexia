package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 10)
    private String plan;

    @Column(length = 5)
    private String idioma;

    @Column(name = "moneda_preferida", length = 3)
    private String monedaPreferida;

    @Column(name = "tema_ui", length = 10)
    private String temaUi;

    @Column(name = "ultimo_acceso")
    private LocalDate ultimoAcceso;
}