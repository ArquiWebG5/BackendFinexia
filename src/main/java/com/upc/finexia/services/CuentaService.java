package com.upc.finexia.services;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;

import java.time.LocalDate;
import java.util.List;

// Contrato del servicio de Cuenta bancaria del usuario.
// HU 30 - Consultar dashboard -> resumenFinanciero
public interface CuentaService {
    CuentaDTO insertar(CuentaDTO cuentaDTO);
    List<CuentaDTO> listarPorUsuario(Long usuarioId);
    CuentaDTO buscarPorId(Long id);
    CuentaDTO actualizar(Long id, CuentaDTO cuentaDTO);
    void eliminar(Long id);
    List<ReporteResumenFinancieroDTO> resumenFinanciero(Long usuarioId, LocalDate desde, LocalDate hasta);
}
