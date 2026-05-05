package com.upc.finexia.services;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.entities.Inversion;
import com.upc.finexia.repositories.InversionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InversionService {

    @Autowired
    private InversionRepositorio inversionRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public InversionDTO insertar(InversionDTO inversionDTO) {
        // Convertimos DTO a Entidad para que el Repo pueda guardarlo
        Inversion entidad = modelMapper.map(inversionDTO, Inversion.class);
        Inversion guardada = inversionRepositorio.save(entidad);
        // Retornamos el DTO
        return modelMapper.map(guardada, InversionDTO.class);
    }

    public List<InversionDTO> listarPorCuenta(Long cuentaId) {
        // El repositorio ya devuelve la lista de DTOs directamente
        return inversionRepositorio.findByCuentaId(cuentaId);
    }

    public InversionDTO actualizar(Long id, InversionDTO inversionDTO) {
        Inversion entidad = inversionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversión no encontrada"));

        // IMPORTANTE: Usa los nombres de campos reales de tu entidad aquí
        // entidad.setCampoReal(inversionDTO.getCampoReal());

        Inversion actualizada = inversionRepositorio.save(entidad);
        return modelMapper.map(actualizada, InversionDTO.class);
    }

    public void eliminar(Long id) {
        inversionRepositorio.deleteById(id);
    }
}