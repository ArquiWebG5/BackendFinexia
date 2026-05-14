package com.upc.finexia.services;

import com.upc.finexia.dtos.MetaDTO;

import java.util.List;

// Contrato del servicio de Metas de ahorro.
// HU 14 - Registrar meta -> insertar
// HU 15 - Editar meta    -> actualizar
// HU 16 - Eliminar meta  -> eliminar
// HU 17 - Visualizar progreso de meta -> buscarPorId (incluye progresoActual)
// HU 18 - Listar metas   -> listarPorUsuario
public interface MetaService {
    MetaDTO insertar(MetaDTO dto);
    List<MetaDTO> listarPorUsuario(Long usuarioId);
    MetaDTO buscarPorId(Long id);
    MetaDTO actualizar(Long id, MetaDTO dto);
    void eliminar(Long id);
}
