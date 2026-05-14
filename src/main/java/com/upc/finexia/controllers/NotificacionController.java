package com.upc.finexia.controllers;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador de Notificaciones financieras.
// HU 29 - Recibir notificaciones financieras (cualquier usuario autenticado).
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // HU 29 - Crear notificacion (la genera el sistema/responsable de finanzas).
    @PostMapping("/notificacion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificacionDTO> insertar(@RequestBody NotificacionDTO notificacionDTO) {
        return ResponseEntity.ok(notificacionService.insertar(notificacionDTO));
    }

    // HU 29 - Listado de notificaciones del usuario autenticado.
    @GetMapping("/notificaciones/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    // HU 29 - Bandeja de no leidas.
    @GetMapping("/notificaciones/no-leidas/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<NotificacionDTO>> listarNoLeidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarNoLeidas(usuarioId));
    }

    // HU 29 - Marcar como leida.
    @PatchMapping("/notificacion/{id}/leer")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
    }
}
