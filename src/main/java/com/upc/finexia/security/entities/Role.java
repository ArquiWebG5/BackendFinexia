package com.upc.finexia.security.entities;

import jakarta.persistence.*;
import lombok.*;

// Entidad de roles (ROLE_ADMIN = responsable de finanzas, ROLE_USER = familiar con acceso de consulta).
// Se usa junto con User para autorizacion basada en roles (HU 19 - Compartir acceso familiar).
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
