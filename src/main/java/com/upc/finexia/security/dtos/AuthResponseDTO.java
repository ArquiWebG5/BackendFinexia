package com.upc.finexia.security.dtos;

import java.util.Set;

// Respuesta de autenticacion: token JWT + roles (HU 02 - Inicio de sesion).
@lombok.Data
public class AuthResponseDTO {
    private String jwt;
    private Set<String> roles;
}
