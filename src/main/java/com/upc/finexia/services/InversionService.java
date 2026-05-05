package com.upc.finexia.services;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Inversiones;
import com.upc.finexia.repositories.InversionesRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upc.finexia.repositories.CuentaRepositorio;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class InversionService {

    @Autowired
    private InversionesRepositorio inversionesRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public InversionDTO insertar(InversionDTO inversionDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(inversionDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Inversiones entidad = modelMapper.map(inversionDTO, Inversiones.class);
        entidad.setCuenta(cuenta);
        return modelMapper.map(inversionesRepositorio.save(entidad), InversionDTO.class);
    }

    public List<InversionDTO> listarPorCuenta(Long cuentaId) {
        return inversionesRepositorio.findByCuentaIdCuenta(cuentaId).stream() // ✅
                .map(i -> modelMapper.map(i, InversionDTO.class))
                .collect(Collectors.toList());
    }

    public InversionDTO actualizar(Long id, InversionDTO inversionDTO) {
        Inversiones entidad = inversionesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversión no encontrada"));
        Inversiones actualizada = inversionesRepositorio.save(entidad);
        return modelMapper.map(actualizada, InversionDTO.class);
    }

    public void eliminar(Long id) {
        inversionesRepositorio.deleteById(id);
    }
}