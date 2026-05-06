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

    @Query("SELECT new com.upc.finexia.dtos.ReporteIngresosRecurrentesDTO(" +
            "i.categoria, " +
            "COUNT(DISTINCT FUNCTION('CONCAT', FUNCTION('YEAR', i.fecha), " +
            "                        FUNCTION('MONTH', i.fecha))), " +
            "AVG(i.monto), " +
            "MIN(i.monto), " +
            "MAX(i.monto)) " +
            "FROM Ingreso i " +
            "WHERE i.cuenta.idCuenta = :cuentaId " +
            "GROUP BY i.categoria " +
            "HAVING COUNT(DISTINCT FUNCTION('CONCAT', FUNCTION('YEAR', i.fecha), " +
            "                               FUNCTION('MONTH', i.fecha))) >= :minMeses " +
            "ORDER BY COUNT(DISTINCT FUNCTION('CONCAT', FUNCTION('YEAR', i.fecha), " +
            "                                 FUNCTION('MONTH', i.fecha))) DESC")
    List<ReporteIngresosRecurrentesDTO> ingresosRecurrentes(
            @Param("cuentaId") Long cuentaId,
            @Param("minMeses") int minMeses);


    // US30: Análisis de ahorro potencial

    @Query("SELECT new com.upc.finexia.dtos.ReporteAnalisisAhorroDTO(" +
            "CONCAT(FUNCTION('YEAR', i.fecha), '-', " +
            "       FUNCTION('LPAD', FUNCTION('MONTH', i.fecha), 2, '0')), " +
            "SUM(i.monto), " +
            "COALESCE((SELECT SUM(e.monto) FROM Egreso e " +
            "          WHERE e.cuenta.idCuenta = :cuentaId " +
            "          AND FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', i.fecha) " +
            "          AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', i.fecha)), 0), " +
            "SUM(i.monto) - COALESCE((SELECT SUM(e.monto) FROM Egreso e " +
            "                         WHERE e.cuenta.idCuenta = :cuentaId " +
            "                         AND FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', i.fecha) " +
            "                         AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', i.fecha)), 0), " +
            "CASE WHEN SUM(i.monto) > 0 " +
            "     THEN (SUM(i.monto) - COALESCE((SELECT SUM(e.monto) FROM Egreso e " +
            "                                     WHERE e.cuenta.idCuenta = :cuentaId " +
            "                                     AND FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', i.fecha) " +
            "                                     AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', i.fecha)), 0)) / SUM(i.monto) * 100 " +
            "     ELSE 0 END) " +
            "FROM Ingreso i " +
            "WHERE i.cuenta.idCuenta = :cuentaId " +
            "GROUP BY FUNCTION('YEAR', i.fecha), FUNCTION('MONTH', i.fecha) " +
            "ORDER BY FUNCTION('YEAR', i.fecha) DESC, FUNCTION('MONTH', i.fecha) DESC")
    List<ReporteAnalisisAhorroDTO> analisisAhorroPotencial(
            @Param("cuentaId") Long cuentaId);
}