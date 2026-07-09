package com.upc.finexia.security.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

// Tipo de cuenta elegido en el registro (HU 01 - Registro de usuario).
// Es la unica fuente de verdad que traduce el tipo de negocio al rol de Spring Security:
//   RESPONSABLE (responsable de finanzas)         -> ROLE_ADMIN -> acceso total (CRUD financiero).
//   FAMILIAR    (familiar con acceso de consulta) -> ROLE_USER  -> solo lectura.
// El responsable es un superconjunto del familiar: todo lo que puede ver el familiar
// tambien lo puede ver el responsable (los endpoints de lectura permiten ambos roles).
public enum TipoUsuario {

    RESPONSABLE("ROLE_ADMIN"),
    FAMILIAR("ROLE_USER");

    private final String roleName;

    TipoUsuario(String roleName) {
        this.roleName = roleName;
    }

    // Nombre del rol de Spring Security asociado (coincide con la tabla roles y data.sql).
    public String getRoleName() {
        return roleName;
    }

    // Parseo tolerante desde el JSON de registro: acepta mayus/minus y espacios.
    // Si el campo llega vacio o nulo devolvemos null para que el servicio aplique el valor por defecto.
    // Ante un valor no soportado devuelve un error claro (Spring lo traduce a HTTP 400).
    @JsonCreator
    public static TipoUsuario desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return TipoUsuario.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "tipoUsuario invalido: '" + valor + "'. Valores permitidos: RESPONSABLE, FAMILIAR");
        }
    }
}
