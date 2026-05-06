package com.upc.finexia.repositories;

import com.upc.finexia.entities.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortafolioRepositorio extends JpaRepository<Portafolio, Long> {
    List<Portafolio> findByUsuarioIdUsuario(Long idUsuario);
}