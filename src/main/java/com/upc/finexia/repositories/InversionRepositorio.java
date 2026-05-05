package com.upc.finexia.repositories;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.entities.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InversionRepositorio extends JpaRepository<Inversion, Long> {
    List<InversionDTO> findByCuentaId(Long cuentaId);
}