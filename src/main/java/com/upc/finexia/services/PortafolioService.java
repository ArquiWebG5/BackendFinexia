package com.upc.finexia.services;

import com.upc.finexia.dtos.PortafolioDTO;

import java.util.List;

// Contrato del servicio de Portafolio agregado.
// HU 25 - Visualizar portafolio (snapshot agregado por usuario).
public interface PortafolioService {
    PortafolioDTO insertar(PortafolioDTO dto);
    List<PortafolioDTO> listarPorUsuario(Long usuarioId);
    void eliminar(Long id);
}
