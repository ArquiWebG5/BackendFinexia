package com.upc.finexia.services;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.entities.Metas;
import com.upc.finexia.entities.Usuarios;
import com.upc.finexia.repositories.MetasRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upc.finexia.repositories.UsuariosRepositorio;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Autowired
    private MetasRepositorio metasRepositorio;

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public MetaDTO insertar(MetaDTO dto) {
        Usuarios usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Metas entidad = modelMapper.map(dto, Metas.class);
        entidad.setUsuario(usuario);
        return modelMapper.map(metasRepositorio.save(entidad), MetaDTO.class);
    }

    public List<MetaDTO> listarPorUsuario(Long usuarioId) {
        return metasRepositorio.findByUsuarioIdUsuario(usuarioId).stream() // ✅
                .map(m -> modelMapper.map(m, MetaDTO.class))
                .collect(Collectors.toList());
    }

    public MetaDTO actualizar(Long id, MetaDTO dto) {
        Metas entidad = metasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        entidad.setNombre(dto.getNombre());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaObjetivo(dto.getFechaObjetivo());
        return modelMapper.map(metasRepositorio.save(entidad), MetaDTO.class);
    }

    public void eliminar(Long id) {
        metasRepositorio.deleteById(id);
    }
}