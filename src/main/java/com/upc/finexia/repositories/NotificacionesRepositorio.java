package com.upc.finexia.repositories;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepositorio extends JpaRepository<Notificaciones, Long> {
    List<Notificaciones> findByUsuarioIdUsuario(Long idUsuario);
    List<Notificaciones> findByUsuarioIdUsuarioAndLeido(Long idUsuario, boolean leido);
}