package com.upc.finexia.services;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) { // US01
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        return modelMapper.map(usuariosRepositorio.save(usuario), UsuarioDTO.class);
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) { // US04
        Usuario usuario = usuariosRepositorio.findById(id)
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