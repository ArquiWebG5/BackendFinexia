package com.upc.finexia.repositories;

import com.upc.finexia.entities.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio de Metas de ahorro (HU 14-18).
@Repository
public interface MetaRepositorio extends JpaRepository<Meta, Long> {
    // HU 18 - Listar metas de ahorro por usuario.
    List<Meta> findByUsuarioIdUsuario(Long idUsuario);
}
