package com.upc.finexia.repositories;

import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.entities.Egresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EgresosRepositorio extends JpaRepository<Egresos, Long> {
    List<Egresos> findByCuentaIdCuenta(Long idCuenta);
    List<Egresos> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Egresos> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);
}
