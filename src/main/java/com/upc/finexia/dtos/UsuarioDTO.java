package com.upc.finexia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// DTO de perfil de Usuario (HU 03 - Visualizar perfil, HU 04 - Actualizar datos).
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String plan;
    private String idioma;
    private String monedaPreferida;
    private String temaUi;
    private LocalDate fechaRegistro;
}
