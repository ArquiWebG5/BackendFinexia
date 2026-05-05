package com.upc.finexia.repositories;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {
    // Proyección directa a DTO
    List<NotificacionDTO> findByUsuarioId(Long usuarioId);
    List<NotificacionDTO> findByUsuarioIdAndLeido(Long usuarioId, boolean leido);
}