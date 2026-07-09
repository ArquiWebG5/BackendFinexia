package com.upc.finexia.security.dtos;

import com.upc.finexia.security.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO de registro (HU 01 - Registro de usuario).
// Combina credenciales de Spring Security (username/password) y datos de perfil del Usuario.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String correo;
    private String plan;
    private String idioma;
    private String monedaPreferida;
    private String temaUi;

    // HU 01 - Tipo de cuenta elegido al registrarse: RESPONSABLE (responsable de finanzas, ROLE_ADMIN)
    // o FAMILIAR (familiar con acceso de consulta, ROLE_USER). Si no se envia, se asume RESPONSABLE.
    private TipoUsuario tipoUsuario;
}
