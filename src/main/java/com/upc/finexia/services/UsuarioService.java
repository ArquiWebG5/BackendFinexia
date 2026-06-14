package com.upc.finexia.services;

import com.upc.finexia.dtos.UsuarioDTO;

import java.util.List;

// Contrato del servicio de perfil de Usuario.
// HU 03 - Visualizar perfil   -> buscarPorId, listar
// HU 04 - Actualizar datos    -> actualizar
// HU 05 - Eliminar cuenta     -> eliminar
public interface UsuarioService {
    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);
    void eliminar(Long id);
    List<UsuarioDTO> listar();
    UsuarioDTO buscarPorId(Long id);
}
