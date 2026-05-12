package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReporteGastosPorCategoriaDTO;
import com.upc.finexia.dtos.ReporteGastosMensualesDTO;
import com.upc.finexia.dtos.ReporteRiesgosGastoDTO;
import com.upc.finexia.dtos.ReporteTopGastosMesDTO;
import com.upc.finexia.entities.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EgresoRepositorio extends JpaRepository<Egreso, Long> {
    List<Egreso> findByCuentaIdCuenta(Long idCuenta);
    List<Egreso> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Egreso> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);


    // US28 & US37: Gastos por categoría

    @Query("SELECT new com.upc.finexia.dtos.ReporteGastosPorCategoriaDTO(" +
            "e.categoria, " +
            "COUNT(e.id), " +
            "SUM(e.monto), " +
            "AVG(e.monto), " +
            "MAX(e.monto), " +
            "MIN(e.monto)) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "AND e.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY e.categoria " +
            "ORDER BY SUM(e.monto) DESC")
    List<ReporteGastosPorCategoriaDTO> gastosPorCategoriaConFechas(
            @Param("cuentaId") Long cuentaId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);


    // US29: Comparar gastos mensuales

    @Query("SELECT new com.upc.finexia.dtos.ReporteGastosMensualesDTO(" +
            "CONCAT(" +
            "CAST(EXTRACT(YEAR FROM e.fecha) AS string), '-', " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM e.fecha) < 10 " +
            "THEN CONCAT('0', CAST(EXTRACT(MONTH FROM e.fecha) AS string)) " +
            "ELSE CAST(EXTRACT(MONTH FROM e.fecha) AS string) " +
            "END), " +
            "SUM(e.monto), " +
            "COUNT(e.id)) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "GROUP BY EXTRACT(YEAR FROM e.fecha), EXTRACT(MONTH FROM e.fecha) " +
            "ORDER BY EXTRACT(YEAR FROM e.fecha) DESC, " +
            "EXTRACT(MONTH FROM e.fecha) DESC")
    List<ReporteGastosMensualesDTO> gastosMensuales(
            @Param("cuentaId") Long cuentaId);
    // US33: Detectar riesgos de gasto excesivo

    @Query("SELECT new com.upc.finexia.dtos.ReporteRiesgosGastoDTO(" +
            "e.categoria, " +
            "SUM(CASE WHEN FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "          AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
            "          THEN e.monto ELSE 0 END), " +
            "AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "          OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "              AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "          THEN e.monto END), " +
            "CASE WHEN AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                    OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                        AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                    THEN e.monto END) IS NULL THEN NULL " +
            "     ELSE CAST((SUM(CASE WHEN FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                         AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
            "                         THEN e.monto ELSE 0 END) - " +
            "              AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                       OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                           AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                       THEN e.monto END)) / " +
            "             AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                      OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                          AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                      THEN e.monto END) * 100 AS Double) END, " +
            "CASE WHEN AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                    OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                        AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                    THEN e.monto END) IS NULL THEN 'SIN_HISTORIAL' " +
            "     WHEN SUM(CASE WHEN FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                   AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
            "                   THEN e.monto ELSE 0 END) > " +
            "          AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                   OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                       AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                   THEN e.monto END) * 1.3 THEN 'RIESGO_ALTO' " +
            "     WHEN SUM(CASE WHEN FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                   AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
            "                   THEN e.monto ELSE 0 END) > " +
            "          AVG(CASE WHEN FUNCTION('YEAR', e.fecha) < FUNCTION('YEAR', CURRENT_DATE) " +
            "                   OR (FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "                       AND FUNCTION('MONTH', e.fecha) < FUNCTION('MONTH', CURRENT_DATE)) " +
            "                   THEN e.monto END) * 1.1 THEN 'RIESGO_MEDIO' " +
            "     ELSE 'NORMAL' END) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "GROUP BY e.categoria")
    List<ReporteRiesgosGastoDTO> detectarRiesgosGasto(@Param("cuentaId") Long cuentaId);


    // US31: Top gastos del mes actual

    @Query("SELECT new com.upc.finexia.dtos.ReporteTopGastosMesDTO(" +
            "e.id, " +
            "e.categoria, " +
            "e.monto, " +
            "e.fecha, " +
            "e.nota, " +
            "e.comprobante) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "AND FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
            "ORDER BY e.monto DESC")
    List<ReporteTopGastosMesDTO> topGastosMesActual(
            @Param("cuentaId") Long cuentaId);
}