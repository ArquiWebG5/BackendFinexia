package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "plan", length = 50)
    private String plan;

    @Column(name = "idioma", length = 50)
    private String idioma;

    @Column(name = "moneda_preferida", length = 50)
    private String monedaPreferida;

    @Column(name = "tema_ui", length = 50)
    private String temaUi;

}