package com.upc.finexia.services;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.entities.Meta;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.MetaRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upc.finexia.repositories.UsuarioRepositorio;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Autowired
    private MetaRepositorio metasRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public MetaDTO insertar(MetaDTO dto) {
        Usuario usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Meta entidad = modelMapper.map(dto, Meta.class);
        entidad.setUsuario(usuario);
        return modelMapper.map(metasRepositorio.save(entidad), MetaDTO.class);
    }

    public List<MetaDTO> listarPorUsuario(Long usuarioId) {
        return metasRepositorio.findByUsuarioIdUsuario(usuarioId).stream() // ✅
                .map(m -> modelMapper.map(m, MetaDTO.class))
                .collect(Collectors.toList());
    }

    public MetaDTO actualizar(Long id, MetaDTO dto) {
        Meta entidad = metasRepositorio.findById(id)
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