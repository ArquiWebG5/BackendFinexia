package com.upc.finexia.repositories;

import com.upc.finexia.entities.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaRepositorio extends JpaRepository<Meta, Long> {
    List<Meta> findByUsuarioIdUsuario(Long idUsuario); // ✅ ya estaba bien
}