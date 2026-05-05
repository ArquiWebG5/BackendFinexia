package com.upc.finexia.repositories;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
    List<CuentaDTO> findByUsuarioId(Long usuarioId);
}