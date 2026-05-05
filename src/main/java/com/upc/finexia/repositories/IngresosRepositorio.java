package com.upc.finexia.repositories;

import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.entities.Ingresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngresosRepositorio extends JpaRepository<Ingresos, Long> {
    List<Ingresos> findByCuentaIdCuenta(Long idCuenta);
    List<Ingresos> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Ingresos> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);
}