package com.upc.finexia.services;

import com.upc.finexia.dtos.*;

import java.time.LocalDate;
import java.util.List;

// Contrato del servicio de Egresos (gastos familiares).
// HU 06 - Registrar gasto       -> insertar
// HU 09 - Editar gasto          -> actualizar
// HU 11 - Eliminar gasto        -> eliminar
// HU 13 - Clasificar por categoria -> listarPorCategoria, gastosPorCategoria
// HU 26 - Reporte de gastos     -> gastosPorCategoria, topGastosMesActual
// HU 27 - Comparar gastos mensuales -> gastosMensuales
public interface EgresoService {
    EgresoDTO insertar(EgresoDTO egresoDTO);
    List<EgresoDTO> listarPorCuenta(Long cuentaId);
    List<EgresoDTO> listarPorCategoria(Long cuentaId, String categoria);
    List<EgresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta);
    EgresoDTO actualizar(Long id, EgresoDTO egresoDTO);
    void eliminar(Long id);
    List<ReporteGastosPorCategoriaDTO> gastosPorCategoria(Long cuentaId, LocalDate desde, LocalDate hasta);
    List<ReporteGastosMensualesDTO> gastosMensuales(Long cuentaId);
    List<ReporteRiesgosGastoDTO> detectarRiesgosGasto(Long cuentaId);
    List<ReporteTopGastosMesDTO> topGastosMesActual(Long cuentaId);
}
