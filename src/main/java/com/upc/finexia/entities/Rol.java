package com.upc.finexia.entities;

import jakarta.persistence.*;
import lombok.*;

// Catalogo de roles de negocio (uso descriptivo).
// La autorizacion real la maneja com.upc.finexia.security.entities.Role asociado a User (HU 02, HU 19).
@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles")
    private Long idRoles;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;
}
