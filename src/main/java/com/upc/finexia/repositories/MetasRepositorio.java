package com.upc.finexia.repositories;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.entities.Metas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetasRepositorio extends JpaRepository<Metas, Long> {
    List<Metas> findByUsuarioIdUsuario(Long idUsuario); // ✅ ya estaba bien
}