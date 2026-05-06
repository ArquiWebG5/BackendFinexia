package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionUsuarioDTO {
    private Long usuarioId;
    private String idioma; // "es", "en", "pt"
    private String monedaBase; // "USD", "PEN", "EUR"
    private String zonaHoraria; // "America/Lima", "America/New_York"
    private Boolean notificacionesEmail;
    private Boolean autenticacionDosFactores;
    private String tema; // "light", "dark"
}