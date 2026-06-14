package com.upc.finexia.repositories;

import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.repositories.projections.ReporteResumenFinancieroProjection;
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

    void deleteByUsuarioIdUsuario(Long idUsuario);

    @Query("SELECT COALESCE(SUM(c.saldoActual), 0) " +
            "FROM Cuenta c " +
            "WHERE c.usuario.idUsuario = :usuarioId")
    Double saldoTotalPorUsuario(@Param("usuarioId") Long usuarioId);

    // HU 30 - Consultar dashboard: resumen financiero por cuenta (totales y balance neto).

    @Query(value =
            "SELECT " +
            "c.id_cuenta AS \"cuentaId\", " +
            "c.nombre_cuenta AS \"nombreCuenta\", " +
            "c.banco_nombre AS \"bancoNombre\", " +
            "c.moneda AS \"moneda\", " +
            "COALESCE((SELECT SUM(i.monto) " +
            "          FROM ingreso i " +
            "          WHERE i.cuenta_id = c.id_cuenta " +
            "          AND i.fecha >= :desde " +
            "          AND i.fecha <= :hasta), 0) AS \"totalIngresos\", " +
            "COALESCE((SELECT SUM(e.monto) " +
            "          FROM egreso e " +
            "          WHERE e.cuenta_id = c.id_cuenta " +
            "          AND e.fecha >= :desde " +
            "          AND e.fecha <= :hasta), 0) AS \"totalEgresos\", " +
            "COALESCE((SELECT SUM(i.monto) " +
            "          FROM ingreso i " +
            "          WHERE i.cuenta_id = c.id_cuenta " +
            "          AND i.fecha >= :desde " +
            "          AND i.fecha <= :hasta), 0) - " +
            "COALESCE((SELECT SUM(e.monto) " +
            "          FROM egreso e " +
            "          WHERE e.cuenta_id = c.id_cuenta " +
            "          AND e.fecha >= :desde " +
            "          AND e.fecha <= :hasta), 0) AS \"balanceNeto\", " +
            "c.saldo_actual AS \"saldoActual\" " +
            "FROM cuenta c " +
            "WHERE c.id_usuario = :usuarioId " +
            "ORDER BY \"balanceNeto\" DESC", nativeQuery = true)
    List<ReporteResumenFinancieroProjection> resumenFinancieroPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);
}
