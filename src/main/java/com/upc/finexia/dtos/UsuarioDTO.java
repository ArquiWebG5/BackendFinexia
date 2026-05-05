package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String password;
    private String plan;
    private String idioma;
    private String monedaPreferida;
    private String temaUi;
    private LocalDate ultimoAcceso;
}