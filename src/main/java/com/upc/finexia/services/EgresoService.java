package com.upc.finexia.services;

import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Egreso;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresoRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EgresoService {

    @Autowired
    private EgresoRepositorio egresoRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public EgresoDTO insertar(EgresoDTO egresoDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(egresoDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Egreso egreso = modelMapper.map(egresoDTO, Egreso.class);
        egreso.setCuenta(cuenta);
        egreso.setCreadoEn(LocalDate.now());
        return modelMapper.map(egresoRepositorio.save(egreso), EgresoDTO.class);
    }

    public List<EgresoDTO> listarPorCuenta(Long cuentaId) {
        return egresoRepositorio.findByCuentaId(cuentaId).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return egresoRepositorio.findByCuentaIdAndCategoria(cuentaId, categoria).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresoRepositorio.findByCuentaIdAndFechaBetween(cuentaId, desde, hasta).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public EgresoDTO actualizar(Long id, EgresoDTO egresoDTO) {
        Egreso egreso = egresoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));
        egreso.setMonto(egresoDTO.getMonto());
        egreso.setFecha(egresoDTO.getFecha());
        egreso.setCategoria(egresoDTO.getCategoria());
        egreso.setNota(egresoDTO.getNota());
        egreso.setComprobante(egresoDTO.getComprobante());
        return modelMapper.map(egresoRepositorio.save(egreso), EgresoDTO.class);
    }

    public void eliminar(Long id) {
        egresoRepositorio.deleteById(id);
    }
}