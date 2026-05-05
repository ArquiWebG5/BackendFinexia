package com.upc.finexia.services;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.entities.Meta;
import com.upc.finexia.repositories.MetaRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaService {
    @Autowired
    private MetaRepositorio metaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    public MetaDTO insertar(MetaDTO dto) {
        Meta entidad = modelMapper.map(dto, Meta.class);
        return modelMapper.map(metaRepositorio.save(entidad), MetaDTO.class);
    }

    public List<MetaDTO> listarPorUsuario(Long usuarioId) {
        return metaRepositorio.findByUsuarioId(usuarioId).stream()
                .map(m -> modelMapper.map(m, MetaDTO.class))
                .collect(Collectors.toList());
    }

    public MetaDTO actualizar(Long id, MetaDTO dto) {
        Meta entidad = metaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        // Actualizar campos usando double según tu cambio previo
        entidad.setNombre(dto.getNombre());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaObjetivo(dto.getFechaObjetivo());

        return modelMapper.map(metaRepositorio.save(entidad), MetaDTO.class);
    }

    public void eliminar(Long id) {
        metaRepositorio.deleteById(id);
    }
}