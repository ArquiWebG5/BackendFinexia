package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReporteAnalisisAhorroDTO;
import com.upc.finexia.dtos.ReporteIngresosRecurrentesDTO;
import com.upc.finexia.entities.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngresoRepositorio extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByCuentaIdCuenta(Long idCuenta);
    List<Ingreso> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Ingreso> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);


    // US34: Detectar ingresos recurrentes

    @Query(value = """
    SELECT
    i.categoria,
    COUNT(DISTINCT to_char(i.fecha, 'YYYY-MM')) AS frecuencia,
    AVG(i.monto) AS promedio,
    MIN(i.monto) AS minimo,
    MAX(i.monto) AS maximo
    FROM ingresos i
    WHERE i.cuenta_id = :cuentaId
    GROUP BY i.categoria
    HAVING COUNT(DISTINCT to_char(i.fecha, 'YYYY-MM')) >= :minMeses
     ORDER BY frecuencia DESC
    """, nativeQuery = true)
    List<ReporteIngresosRecurrentesDTO> ingresosRecurrentes(
            @Param("cuentaId") Long cuentaId,
            @Param("minMeses") int minMeses);

    // US30: Análisis de ahorro potencial
    @Query(value = """
    WITH ingresos_mensuales AS (
    SELECT
        date_trunc('month', i.fecha) AS mes,
        SUM(i.monto) AS total_ingresos
    FROM ingresos i
    WHERE i.cuenta_id = :cuentaId
    GROUP BY date_trunc('month', i.fecha)
    ),
    egresos_mensuales AS (
    SELECT
        date_trunc('month', e.fecha) AS mes,
        SUM(e.monto) AS total_egresos
    FROM egresos e
    WHERE e.cuenta_id = :cuentaId
    GROUP BY date_trunc('month', e.fecha))
    SELECT
    to_char(i.mes, 'YYYY-MM') AS periodo,
    i.total_ingresos,
    COALESCE(e.total_egresos, 0) AS total_egresos,
    (i.total_ingresos - COALESCE(e.total_egresos, 0)) AS ahorro,
    CASE
        WHEN i.total_ingresos > 0 THEN
            ((i.total_ingresos - COALESCE(e.total_egresos, 0)) / i.total_ingresos) * 100
        ELSE 0
    END AS porcentaje_ahorro
    FROM ingresos_mensuales i
    LEFT JOIN egresos_mensuales e ON i.mes = e.mes
    ORDER BY i.mes DESC
    """, nativeQuery = true)
    List<ReporteAnalisisAhorroDTO> analisisAhorroPotencial(
            @Param("cuentaId") Long cuentaId);
}