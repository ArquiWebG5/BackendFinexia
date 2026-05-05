package com.upc.finexia.services;

import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Ingreso;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.IngresoRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngresoService {

    @Autowired
    private IngresoRepositorio ingresoRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public IngresoDTO insertar(IngresoDTO ingresoDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(ingresoDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Ingreso ingreso = modelMapper.map(ingresoDTO, Ingreso.class);
        ingreso.setCuenta(cuenta);
        ingreso.setCreadoEn(LocalDate.now());
        return modelMapper.map(ingresoRepositorio.save(ingreso), IngresoDTO.class);
    }

    public List<IngresoDTO> listarPorCuenta(Long cuentaId) { // US09
        return ingresoRepositorio.findByCuentaId(cuentaId).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<IngresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return ingresoRepositorio.findByCuentaIdAndCategoria(cuentaId, categoria).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<IngresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return ingresoRepositorio.findByCuentaIdAndFechaBetween(cuentaId, desde, hasta).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public IngresoDTO actualizar(Long id, IngresoDTO ingresoDTO) {
        Ingreso ingreso = ingresoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));
        ingreso.setMonto(ingresoDTO.getMonto());
        ingreso.setFecha(ingresoDTO.getFecha());
        ingreso.setCategoria(ingresoDTO.getCategoria());
        ingreso.setNota(ingresoDTO.getNota());
        ingreso.setComprobante(ingresoDTO.getComprobante());
        return modelMapper.map(ingresoRepositorio.save(ingreso), IngresoDTO.class);
    }

    public void eliminar(Long id) {
        ingresoRepositorio.deleteById(id);
    }
}