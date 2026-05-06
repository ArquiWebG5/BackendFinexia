package com.upc.finexia.services;

import com.upc.finexia.dtos.*;
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
    private EgresoRepositorio egresosRepositorio;

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
        return modelMapper.map(egresosRepositorio.save(egreso), EgresoDTO.class);
    }

    public List<EgresoDTO> listarPorCuenta(Long cuentaId) {
        return egresosRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return egresosRepositorio.findByCuentaIdCuentaAndCategoria(cuentaId, categoria).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresosRepositorio.findByCuentaIdCuentaAndFechaBetween(cuentaId, desde, hasta).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public EgresoDTO actualizar(Long id, EgresoDTO egresoDTO) {
        Egreso egreso = egresosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));
        egreso.setMonto(egresoDTO.getMonto());
        egreso.setFecha(egresoDTO.getFecha());
        egreso.setCategoria(egresoDTO.getCategoria());
        egreso.setNota(egresoDTO.getNota());
        egreso.setComprobante(egresoDTO.getComprobante());
        return modelMapper.map(egresosRepositorio.save(egreso), EgresoDTO.class);
    }

    public void eliminar(Long id) {
        egresosRepositorio.deleteById(id);
    }

    //US28 & US37: Gastos por categoría

    public List<ReporteGastosPorCategoriaDTO> gastosPorCategoria(
            Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresosRepositorio.gastosPorCategoria(cuentaId, desde, hasta);
    }

    //US29: Comparar gastos mensuales

    public List<ReporteGastosMensualesDTO> gastosMensuales(Long cuentaId) {
        return egresosRepositorio.gastosMensuales(cuentaId);
    }

    //US33: Detectar riesgos de gasto

    public List<ReporteRiesgosGastoDTO> detectarRiesgosGasto(Long cuentaId) {
        return egresosRepositorio.detectarRiesgosGasto(cuentaId);
    }

    //US31: Top gastos del mes actual

    public List<ReporteTopGastosMesDTO> topGastosMesActual(Long cuentaId) {
        return egresosRepositorio.topGastosMesActual(cuentaId);
    }
}