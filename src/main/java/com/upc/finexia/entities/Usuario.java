package com.upc.finexia.entities;
import jakarta.persistence.*;
import lombok.*;
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
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 150)
    private String apellido;

    @Column(name = "correo", nullable = false,unique = true, length = 100)
    private String correo;

    @Column(name = "contraseña", length = 50)
    private String contraseña;

    @Column(name = "plan", length = 50)
    private String plan;

    @Column(name = "idioma", length = 50)
    private String idioma;

    @Column(name = "moneda_preferida", length = 50)
    private String monedaPreferida;

    @Column(name = "tema_ui", length = 50)
    private String temaUi;

    @Column(name = "fecha_registro", length = 50)
    private LocalDate fechaRegistro;
}