package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repositorio de Cuentas. Alimenta HU 30 (dashboard) con resumen financiero por cuenta.
@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
    // Listado de cuentas por usuario (apoyo a HU 30 - dashboard).
    List<Cuenta> findByUsuarioIdUsuario(Long idUsuario);

    // HU 30 - Consultar dashboard: resumen financiero por cuenta (totales y balance neto).

    @Query(value =
            "SELECT " +
            "c.id_cuenta, " +
            "c.nombre_cuenta, " +
            "c.banco_nombre, " +
            "c.moneda, " +
            "COALESCE((SELECT SUM(i.monto) " +
            "          FROM ingreso i " +
            "          WHERE i.cuenta_id = c.id_cuenta), 0) AS total_ingresos, " +
            "COALESCE((SELECT SUM(e.monto) " +
            "          FROM egreso e " +
            "          WHERE e.cuenta_id = c.id_cuenta), 0) AS total_egresos, " +
            "COALESCE((SELECT SUM(i.monto) " +
            "          FROM ingreso i " +
            "          WHERE i.cuenta_id = c.id_cuenta), 0) - " +
            "COALESCE((SELECT SUM(e.monto) " +
            "          FROM egreso e " +
            "          WHERE e.cuenta_id = c.id_cuenta), 0) AS balance_neto, " +
            "c.saldo_actual " +
            "FROM cuenta c " +
            "WHERE c.id_usuario = :usuarioId " +
            "ORDER BY balance_neto DESC", nativeQuery = true)
    List<ReporteResumenFinancieroDTO> resumenFinancieroPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);
}