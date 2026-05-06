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

    // US32: Resumen financiero del usuario (SQL NATIVA)

    @Query(value = """
            SELECT 
                c.id_cuenta,
                c.nombre_cuenta,
                c.banco_nombre,
                c.moneda,
                COALESCE(SUM(CASE WHEN i.id IS NOT NULL THEN i.monto ELSE 0 END), 0) AS total_ingresos,
                COALESCE(SUM(CASE WHEN e.id IS NOT NULL THEN e.monto ELSE 0 END), 0) AS total_egresos,
                COALESCE(SUM(CASE WHEN i.id IS NOT NULL THEN i.monto ELSE 0 END), 0) - 
                COALESCE(SUM(CASE WHEN e.id IS NOT NULL THEN e.monto ELSE 0 END), 0) AS balance_neto,
                c.saldo_actual
            FROM cuenta c
            LEFT JOIN ingresos i ON c.id_cuenta = i.cuenta_id
                AND (:desde IS NULL OR i.fecha >= :desde)
                AND (:hasta IS NULL OR i.fecha <= :hasta)
            LEFT JOIN egresos e ON c.id_cuenta = e.cuenta_id
                AND (:desde IS NULL OR e.fecha >= :desde)
                AND (:hasta IS NULL OR e.fecha <= :hasta)
            WHERE c.usuario_id = :usuarioId
            GROUP BY c.id_cuenta, c.nombre_cuenta, c.banco_nombre, c.moneda, c.saldo_actual
            ORDER BY balance_neto DESC
            """, nativeQuery = true)
    List<ReporteResumenFinancieroDTO> resumenFinancieroPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);
}