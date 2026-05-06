package com.upc.finexia.repositories;

import com.upc.finexia.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdUsuario(Long idUsuario);
    List<Notificacion> findByUsuarioIdUsuarioAndLeido(Long idUsuario, boolean leido);
}