package com.upc.finexia.services;

import com.upc.finexia.dtos.PortafolioDTO;
import com.upc.finexia.entities.Portafolio;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.PortafolioRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortafolioService {

    @Autowired
    private PortafolioRepositorio portafoliosRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public PortafolioDTO insertar(PortafolioDTO dto) {
        Usuario usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Portafolio entidad = modelMapper.map(dto, Portafolio.class);
        entidad.setUsuario(usuario);
        return modelMapper.map(portafoliosRepositorio.save(entidad), PortafolioDTO.class);
    }

    public List<PortafolioDTO> listarPorUsuario(Long usuarioId) {
        return portafoliosRepositorio.findByUsuarioIdUsuario(usuarioId).stream() // ✅
                .map(p -> modelMapper.map(p, PortafolioDTO.class))
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        portafoliosRepositorio.deleteById(id);
    }
}