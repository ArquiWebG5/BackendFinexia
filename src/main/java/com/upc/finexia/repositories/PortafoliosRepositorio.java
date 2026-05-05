package com.upc.finexia.repositories;

import com.upc.finexia.dtos.PortafolioDTO;
import com.upc.finexia.entities.Portafolios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortafoliosRepositorio extends JpaRepository<Portafolios, Long> {
    List<Portafolios> findByUsuarioIdUsuario(Long idUsuario);
}