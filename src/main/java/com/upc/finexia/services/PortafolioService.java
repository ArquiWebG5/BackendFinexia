package com.upc.finexia.services;

import com.upc.finexia.dtos.PortafolioDTO;
import com.upc.finexia.entities.Portafolio;
import com.upc.finexia.repositories.PortafolioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortafolioService {
    @Autowired
    private PortafolioRepositorio portafolioRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    public PortafolioDTO insertar(PortafolioDTO dto) {
        Portafolio entidad = modelMapper.map(dto, Portafolio.class);
        return modelMapper.map(portafolioRepositorio.save(entidad), PortafolioDTO.class);
    }

    public List<PortafolioDTO> listarPorUsuario(Long usuarioId) {
        return portafolioRepositorio.findByUsuarioId(usuarioId).stream()
                .map(p -> modelMapper.map(p, PortafolioDTO.class))
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        portafolioRepositorio.deleteById(id);
    }
}