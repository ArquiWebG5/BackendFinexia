package com.upc.finexia.repositories;

import com.upc.finexia.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio de Notificaciones (HU 29 - Recibir notificaciones financieras).
@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {
    // HU 29 - Listado por usuario.
    List<Notificacion> findByUsuarioIdUsuario(Long idUsuario);

    // HU 29 - Bandeja de no leidas.
    List<Notificacion> findByUsuarioIdUsuarioAndLeido(Long idUsuario, boolean leido);

    void deleteByUsuarioIdUsuario(Long idUsuario);
}
