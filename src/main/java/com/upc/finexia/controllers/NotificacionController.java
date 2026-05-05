package com.upc.finexia.controllers;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    public NotificacionDTO insertar(@RequestBody NotificacionDTO notificacionDTO) {
        return notificacionService.insertar(notificacionDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<NotificacionDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return notificacionService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/no-leidas/{usuarioId}")
    public List<NotificacionDTO> listarNoLeidas(@PathVariable Long usuarioId) {
        return notificacionService.listarNoLeidas(usuarioId);
    }

    @PatchMapping("/{id}/leer")
    public void marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
    }
}