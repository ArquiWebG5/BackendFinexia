package com.upc.finexia.repositories;

import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.entities.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngresoRepositorio extends JpaRepository<Ingreso, Long> {
    List<IngresoDTO> findByCuentaId(Long cuentaId); // Error línea 38
    List<IngresoDTO> findByCuentaIdAndCategoria(Long cuentaId, String categoria); // Error línea 44
    List<IngresoDTO> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate inicio, LocalDate fin); // Error línea 50
}
