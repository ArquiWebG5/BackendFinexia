package com.upc.finexia.controllers;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador del perfil de Usuario.
// HU 03 - Visualizar perfil   -> GET /api/usuario/{id}   (familiar consulta y responsable)
// HU 04 - Actualizar datos    -> PUT /api/usuario/{id}   (solo responsable de finanzas)
// HU 05 - Eliminar cuenta     -> DELETE /api/usuario/{id} (solo responsable de finanzas)
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // HU 03 - Visualizar perfil de usuario.
    @GetMapping("/usuario/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // Listado general (apoya a HU 03).
    @GetMapping("/usuarios")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // HU 04 - Actualizar datos de usuario.
    @PutMapping("/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuarioDTO));
    }

    // HU 05 - Eliminar cuenta.
    @DeleteMapping("/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
