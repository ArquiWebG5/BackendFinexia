package com.upc.finexia.repositories;

import com.upc.finexia.entities.VentaActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaActivoRepositorio extends JpaRepository<VentaActivo, Long> {
    List<VentaActivo> findByCuentaIdCuenta(Long idCuenta);
    void deleteByInversionId(Long inversionId);
    void deleteByCuentaIdCuenta(Long idCuenta);
}
