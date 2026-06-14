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

// Repositorio de Egresos. Soporta HU 06, 09, 11, 13, 26, 27.
@Repository
public interface EgresoRepositorio extends JpaRepository<Egreso, Long> {
    List<Egreso> findByCuentaIdCuenta(Long idCuenta);

    // HU 13 - Clasificar gastos por categoria.
    List<Egreso> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Egreso> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);
    void deleteByCuentaIdCuenta(Long idCuenta);


    // HU 13 / HU 26 - Reporte de gastos agregados por categoria con rango de fechas.

    @Query("SELECT new com.upc.finexia.dtos.ReporteGastosPorCategoriaDTO(" +
            "e.categoria, " +
            "COUNT(e.id), " +
            "SUM(e.monto), " +
            "AVG(e.monto), " +
            "MAX(e.monto), " +
            "MIN(e.monto)) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "AND e.fecha >= :desde " +
            "AND e.fecha <= :hasta " +
            "GROUP BY e.categoria " +
            "ORDER BY SUM(e.monto) DESC")
    List<ReporteGastosPorCategoriaDTO> gastosPorCategoriaConFechas(
            @Param("cuentaId") Long cuentaId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    // HU 27 - Comparar gastos mensuales.

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

    // Detecta riesgos de sobregasto comparando mes actual con promedio historico
    // (apoyo a HU 26 - reporte de gastos / HU 30 - dashboard).

    @Query("SELECT new com.upc.finexia.dtos.ReporteRiesgosGastoDTO(" +
            "e.categoria, " +

            // gasto actual
            "SUM(CASE " +
            "WHEN FUNCTION('DATE_PART', 'YEAR', e.fecha) = FUNCTION('DATE_PART', 'YEAR', CURRENT_DATE) " +
            "AND FUNCTION('DATE_PART', 'MONTH', e.fecha) = FUNCTION('DATE_PART', 'MONTH', CURRENT_DATE) " +
            "THEN e.monto ELSE 0 END), " +

            // promedio histórico
            "AVG(CASE " +
            "WHEN e.fecha < CURRENT_DATE " +
            "THEN e.monto END), " +

            // variación porcentual
            "CASE " +
            "WHEN AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END) IS NULL " +
            "THEN 0.0 " +
            "ELSE ( " +
            "(SUM(CASE " +
            "WHEN FUNCTION('DATE_PART', 'YEAR', e.fecha) = FUNCTION('DATE_PART', 'YEAR', CURRENT_DATE) " +
            "AND FUNCTION('DATE_PART', 'MONTH', e.fecha) = FUNCTION('DATE_PART', 'MONTH', CURRENT_DATE) " +
            "THEN e.monto ELSE 0 END) " +
            "- AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END)) " +
            "/ AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END) * 100 " +
            ") END, " +

            // nivel riesgo
            "CASE " +
            "WHEN AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END) IS NULL " +
            "THEN 'SIN_HISTORIAL' " +

            "WHEN SUM(CASE " +
            "WHEN FUNCTION('DATE_PART', 'YEAR', e.fecha) = FUNCTION('DATE_PART', 'YEAR', CURRENT_DATE) " +
            "AND FUNCTION('DATE_PART', 'MONTH', e.fecha) = FUNCTION('DATE_PART', 'MONTH', CURRENT_DATE) " +
            "THEN e.monto ELSE 0 END) " +
            "> AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END) * 1.3 " +
            "THEN 'RIESGO_ALTO' " +

            "WHEN SUM(CASE " +
            "WHEN FUNCTION('DATE_PART', 'YEAR', e.fecha) = FUNCTION('DATE_PART', 'YEAR', CURRENT_DATE) " +
            "AND FUNCTION('DATE_PART', 'MONTH', e.fecha) = FUNCTION('DATE_PART', 'MONTH', CURRENT_DATE) " +
            "THEN e.monto ELSE 0 END) " +
            "> AVG(CASE WHEN e.fecha < CURRENT_DATE THEN e.monto END) * 1.1 " +
            "THEN 'RIESGO_MEDIO' " +

            "ELSE 'NORMAL' END) " +

            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "GROUP BY e.categoria")
    List<ReporteRiesgosGastoDTO> detectarRiesgosGasto(
            @Param("cuentaId") Long cuentaId);

    // HU 26 - Top gastos del mes actual (reporte de gastos).

    @Query("SELECT new com.upc.finexia.dtos.ReporteTopGastosMesDTO(" +
            "e.id, " +
            "e.categoria, " +
            "e.monto, " +
            "e.fecha, " +
            "e.nota, " +
            "e.comprobante) " +
            "FROM Egreso e " +
            "WHERE e.cuenta.idCuenta = :cuentaId " +
            "AND FUNCTION('DATE_PART', 'YEAR', e.fecha) = FUNCTION('DATE_PART', 'YEAR', CURRENT_DATE) " +
            "AND FUNCTION('DATE_PART', 'MONTH', e.fecha) = FUNCTION('DATE_PART', 'MONTH', CURRENT_DATE) " +
            "ORDER BY e.monto DESC")
    List<ReporteTopGastosMesDTO> topGastosMesActual(
            @Param("cuentaId") Long cuentaId);
}
