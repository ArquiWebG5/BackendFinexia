package com.upc.finexia.services;

import com.upc.finexia.dtos.NotificacionDTO;

import java.util.List;

// Contrato del servicio de Notificaciones.
// HU 29 - Recibir notificaciones financieras -> insertar, listarPorUsuario, listarNoLeidas, marcarLeida.
public interface NotificacionService {
    NotificacionDTO insertar(NotificacionDTO dto);
    List<NotificacionDTO> listarPorUsuario(Long usuarioId);
    List<NotificacionDTO> listarNoLeidas(Long usuarioId);
    void marcarLeida(Long id);
}
