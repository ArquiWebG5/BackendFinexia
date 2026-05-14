package com.upc.finexia.services;

import com.upc.finexia.dtos.*;

import java.util.List;

// Contrato del servicio de Inversiones.
// HU 21 - Registrar inversion  -> insertar
// HU 22 - Editar inversion     -> actualizar
// HU 23 - Eliminar inversion   -> eliminar
// HU 24 - Listar inversiones   -> listarPorCuenta
// HU 25 - Visualizar portafolio -> reportePortafolio, topPosicionesPortafolio
public interface InversionService {
    InversionDTO insertar(InversionDTO dto);
    List<InversionDTO> listarPorCuenta(Long cuentaId);
    InversionDTO actualizar(Long id, InversionDTO dto);
    void eliminar(Long id);
    List<ReportePortafolioDTO> reportePortafolio(Long usuarioId);
    List<ReporteTopPosicionesPortafolioDTO> topPosicionesPortafolio(Long usuarioId, int top);
}
