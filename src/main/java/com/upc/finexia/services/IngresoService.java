package com.upc.finexia.services;

import com.upc.finexia.dtos.*;
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
    private IngresoRepositorio ingresosRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;


    public IngresoDTO insertar(IngresoDTO ingresoDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(ingresoDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Ingreso ingresos = modelMapper.map(ingresoDTO, Ingreso.class);
        ingresos.setCuenta(cuenta);
        ingresos.setCreadoEn(LocalDate.now());
        return modelMapper.map(ingresosRepositorio.save(ingresos), IngresoDTO.class);
    }

    public List<IngresoDTO> listarPorCuenta(Long cuentaId) {
        return ingresosRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<IngresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return ingresosRepositorio.findByCuentaIdCuentaAndCategoria(cuentaId, categoria).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<IngresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return ingresosRepositorio.findByCuentaIdCuentaAndFechaBetween(cuentaId, desde, hasta).stream()
                .map(i -> modelMapper.map(i, IngresoDTO.class))
                .collect(Collectors.toList());
    }

    public IngresoDTO actualizar(Long id, IngresoDTO ingresoDTO) {
        Ingreso ingresos = ingresosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));
        ingresos.setMonto(ingresoDTO.getMonto());
        ingresos.setFecha(ingresoDTO.getFecha());
        ingresos.setCategoria(ingresoDTO.getCategoria());
        ingresos.setNota(ingresoDTO.getNota());
        ingresos.setComprobante(ingresoDTO.getComprobante());
        return modelMapper.map(ingresosRepositorio.save(ingresos), IngresoDTO.class);
    }

    public void eliminar(Long id) {
        ingresosRepositorio.deleteById(id);
    }

    //US34: Ingresos recurrentes

    public List<ReporteIngresosRecurrentesDTO> ingresosRecurrentes(
            Long cuentaId, int minMeses) {
        return ingresosRepositorio.ingresosRecurrentes(cuentaId, minMeses);
    }

    //US30: Análisis de ahorro potencial

    public List<ReporteAnalisisAhorroDTO> analisisAhorro(Long cuentaId) {
        return ingresosRepositorio.analisisAhorroPotencial(cuentaId);
    }
}