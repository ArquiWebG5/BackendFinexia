package com.upc.finexia.security.dtos;

import lombok.Getter;
import lombok.Setter;

// DTO para el cambio de contraseña del usuario autenticado.
@Getter
@Setter
public class CambioPasswordDTO {
    private String actual;
    private String nueva;
}
