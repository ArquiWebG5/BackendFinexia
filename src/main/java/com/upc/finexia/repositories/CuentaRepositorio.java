package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByUsuarioIdUsuario(Long idUsuario);

    // US32: Resumen financiero del usuario

    @Query("SELECT new com.upc.finexia.dtos.ReporteResumenFinancieroDTO(" +
            "c.idCuenta, " +
            "c.nombreCuenta, " +
            "c.bancoNombre, " +
            "c.moneda, " +
            "COALESCE(SUM(CASE WHEN i.id IS NOT NULL THEN i.monto ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN e.id IS NOT NULL THEN e.monto ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN i.id IS NOT NULL THEN i.monto ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN e.id IS NOT NULL THEN e.monto ELSE 0 END), 0), " +
            "c.saldoActual) " +
            "FROM Cuenta c " +
            "LEFT JOIN c.ingresos i " +
            "  AND (:desde IS NULL OR i.fecha >= :desde) " +
            "  AND (:hasta IS NULL OR i.fecha <= :hasta) " +
            "LEFT JOIN c.egresos e " +
            "  AND (:desde IS NULL OR e.fecha >= :desde) " +
            "  AND (:hasta IS NULL OR e.fecha <= :hasta) " +
            "WHERE c.usuario.idUsuario = :usuarioId " +
            "GROUP BY c.idCuenta, c.nombreCuenta, c.bancoNombre, c.moneda, c.saldoActual " +
            "ORDER BY (COALESCE(SUM(CASE WHEN i.id IS NOT NULL THEN i.monto ELSE 0 END), 0) - " +
            "         COALESCE(SUM(CASE WHEN e.id IS NOT NULL THEN e.monto ELSE 0 END), 0)) DESC")
    List<ReporteResumenFinancieroDTO> resumenFinancieroPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);
}