package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// Perfil de negocio del usuario (datos personales/preferencias).
// Las credenciales (username/password) viven en com.upc.finexia.security.entities.User.
// Cubre HU 03 (visualizar perfil) y HU 04 (actualizar datos).
@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 150)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "plan", length = 50)
    private String plan;

    @Column(name = "idioma", length = 50)
    private String idioma;

    @Column(name = "moneda_preferida", length = 50)
    private String monedaPreferida;

    @Column(name = "tema_ui", length = 50)
    private String temaUi;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;
}
