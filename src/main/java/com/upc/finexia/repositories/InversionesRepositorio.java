package com.upc.finexia.repositories;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.entities.Inversiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InversionesRepositorio extends JpaRepository<Inversiones, Long> {
    List<Inversiones> findByCuentaIdCuenta(Long idCuenta);
}