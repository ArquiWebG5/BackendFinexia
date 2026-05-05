package com.upc.finexia.services;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.entities.Usuarios;
import com.upc.finexia.repositories.UsuariosRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) { // US01
        Usuarios usuario = modelMapper.map(usuarioDTO, Usuarios.class);
        return modelMapper.map(usuariosRepositorio.save(usuario), UsuarioDTO.class);
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) { // US04
        Usuarios usuario = usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombreCompleto(usuarioDTO.getNombreCompleto());
        usuario.setEmail(usuarioDTO.getEmail());
        return modelMapper.map(usuariosRepositorio.save(usuario), UsuarioDTO.class);
    }

    public void eliminar(Long id) { // US05
        usuariosRepositorio.deleteById(id);
    }

    public List<UsuarioDTO> listar() {
        return usuariosRepositorio.findAll().stream()
                .map(u -> modelMapper.map(u, UsuarioDTO.class))
                .collect(Collectors.toList());
    }
}