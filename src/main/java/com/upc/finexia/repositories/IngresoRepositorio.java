package com.upc.finexia.repositories;

import com.upc.finexia.entities.Ingreso;
import com.upc.finexia.repositories.projections.ReporteAnalisisAhorroProjection;
import com.upc.finexia.repositories.projections.ReporteIngresosRecurrentesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repositorio de Ingresos. Soporta HU 07, 08, 10, 12.
@Repository
public interface IngresoRepositorio extends JpaRepository<Ingreso, Long> {
    // HU 08 - Visualizar ingresos por cuenta.
    List<Ingreso> findByCuentaIdCuenta(Long idCuenta);
    List<Ingreso> findByCuentaIdCuentaAndCategoria(Long idCuenta, String categoria);
    List<Ingreso> findByCuentaIdCuentaAndFechaBetween(Long idCuenta, LocalDate inicio, LocalDate fin);
    void deleteByCuentaIdCuenta(Long idCuenta);


    // Detecta ingresos recurrentes por categoria (apoyo a HU 30 - dashboard).

    @Query(value = """
    SELECT
    i.categoria AS "categoria",
    COUNT(DISTINCT to_char(i.fecha, 'YYYY-MM')) AS "mesesPresentes",
    AVG(i.monto) AS "montoPromedio",
    MIN(i.monto) AS "montoMinimo",
    MAX(i.monto) AS "montoMaximo"
    FROM ingreso i
    WHERE i.cuenta_id = :cuentaId
    GROUP BY i.categoria
    HAVING COUNT(DISTINCT to_char(i.fecha, 'YYYY-MM')) >= :minMeses
     ORDER BY "mesesPresentes" DESC
    """, nativeQuery = true)
    List<ReporteIngresosRecurrentesProjection> ingresosRecurrentes(
            @Param("cuentaId") Long cuentaId,
            @Param("minMeses") int minMeses);

    // Analisis de ahorro mensual (apoyo a HU 30 - dashboard).
    // FULL OUTER JOIN: un mes con solo egresos (sin ingresos ese mes) o viceversa
    // debe seguir apareciendo en el reporte, no desaparecer (LEFT JOIN perdia esos meses).
    @Query(value = """
    WITH ingresos_mensuales AS (
    SELECT
        date_trunc('month', i.fecha) AS mes,
        SUM(i.monto) AS total_ingresos
    FROM ingreso i
    WHERE i.cuenta_id = :cuentaId
    GROUP BY date_trunc('month', i.fecha)
    ),
    egresos_mensuales AS (
    SELECT
        date_trunc('month', e.fecha) AS mes,
        SUM(e.monto) AS total_egresos
    FROM egreso e
    WHERE e.cuenta_id = :cuentaId
    GROUP BY date_trunc('month', e.fecha))
    SELECT
    to_char(COALESCE(i.mes, e.mes), 'YYYY-MM') AS "mes",
    COALESCE(i.total_ingresos, 0) AS "totalIngresos",
    COALESCE(e.total_egresos, 0) AS "totalEgresos",
    (COALESCE(i.total_ingresos, 0) - COALESCE(e.total_egresos, 0)) AS "ahorroReal",
    CASE
        WHEN COALESCE(i.total_ingresos, 0) > 0 THEN
            ((COALESCE(i.total_ingresos, 0) - COALESCE(e.total_egresos, 0)) / i.total_ingresos) * 100
        ELSE 0
    END AS "tasaAhorroPct"
    FROM ingresos_mensuales i
    FULL OUTER JOIN egresos_mensuales e ON i.mes = e.mes
    ORDER BY COALESCE(i.mes, e.mes) DESC
    """, nativeQuery = true)
    List<ReporteAnalisisAhorroProjection> analisisAhorroPotencial(
            @Param("cuentaId") Long cuentaId);
}
