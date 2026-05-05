package com.upc.finexia.repositories;

import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.entities.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EgresoRepositorio extends JpaRepository<Egreso, Long> {
    List<EgresoDTO> findByCuentaId(Long cuentaId);

    List<EgresoDTO> findByCuentaIdAndCategoria(Long cuentaId, String categoria);

    List<EgresoDTO> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate inicio, LocalDate fin);
}
