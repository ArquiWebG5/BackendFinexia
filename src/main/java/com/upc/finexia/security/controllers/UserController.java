package com.upc.finexia.security.controllers;

import com.upc.finexia.security.dtos.CambioPasswordDTO;
import com.upc.finexia.security.entities.Role;
import com.upc.finexia.security.entities.User;
import com.upc.finexia.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

// Administracion de usuarios y roles del lado de seguridad.
// HU 19 - Compartir acceso familiar: el responsable (ADMIN) asigna ROLE_USER a otro user (familiar consulta).
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder bcrypt;

    // Alta directa de un user (solo administradores).
    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(@RequestBody User user) {
        String bcryptPassword = bcrypt.encode(user.getPassword());
        user.setPassword(bcryptPassword);
        userService.save(user);
    }

    // Crea un rol nuevo (mantenimiento). Para seed inicial se usa import.sql.
    @PostMapping("/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public void createRol(@RequestBody Role rol) {
        userService.grabar(rol);
    }

    // HU 19 - Compartir acceso familiar: vincula un rol existente (p.ej. ROLE_USER) a un user.
    @PostMapping("/save/{user_id}/{rol_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> saveUseRol(@PathVariable("user_id") Long user_id,
                                              @PathVariable("rol_id") Long rol_id) {
        return new ResponseEntity<Integer>(userService.insertUserRol(user_id, rol_id), HttpStatus.OK);
    }

    // Cambio de contraseña del usuario autenticado. Valida la contraseña actual antes de actualizar.
    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> cambiarPassword(Principal principal, @RequestBody CambioPasswordDTO dto) {
        try {
            userService.cambiarPassword(principal.getName(), dto.getActual(), dto.getNueva());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
