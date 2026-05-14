package com.upc.finexia.services;

import com.upc.finexia.dtos.*;

import java.time.LocalDate;
import java.util.List;

// Contrato del servicio de Ingresos.
// HU 07 - Registrar ingreso   -> insertar
// HU 08 - Visualizar ingresos -> listarPorCuenta
// HU 10 - Editar ingreso      -> actualizar
// HU 12 - Eliminar ingreso    -> eliminar
public interface IngresoService {
    IngresoDTO insertar(IngresoDTO ingresoDTO);
    List<IngresoDTO> listarPorCuenta(Long cuentaId);
    List<IngresoDTO> listarPorCategoria(Long cuentaId, String categoria);
    List<IngresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta);
    IngresoDTO actualizar(Long id, IngresoDTO ingresoDTO);
    void eliminar(Long id);
    List<ReporteIngresosRecurrentesDTO> ingresosRecurrentes(Long cuentaId, int minMeses);
    List<ReporteAnalisisAhorroDTO> analisisAhorro(Long cuentaId);
}
