package com.upc.finexia.services;

import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Egresos;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresosRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EgresoService {

    @Autowired
    private EgresosRepositorio egresosRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public EgresoDTO insertar(EgresoDTO egresoDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(egresoDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Egresos egresos = modelMapper.map(egresoDTO, Egresos.class);
        egresos.setCuenta(cuenta);
        egresos.setCreadoEn(LocalDate.now());
        return modelMapper.map(egresosRepositorio.save(egresos), EgresoDTO.class);
    }

    public List<EgresoDTO> listarPorCuenta(Long cuentaId) {
        return egresosRepositorio.findByCuentaIdCuenta(cuentaId).stream() // ✅
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return egresosRepositorio.findByCuentaIdCuentaAndCategoria(cuentaId, categoria).stream() // ✅
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EgresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresosRepositorio.findByCuentaIdCuentaAndFechaBetween(cuentaId, desde, hasta).stream() // ✅
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    public EgresoDTO actualizar(Long id, EgresoDTO egresoDTO) {
        Egresos egresos = egresosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));
        egresos.setMonto(egresoDTO.getMonto());
        egresos.setFecha(egresoDTO.getFecha());
        egresos.setCategoria(egresoDTO.getCategoria());
        egresos.setNota(egresoDTO.getNota());
        egresos.setComprobante(egresoDTO.getComprobante());
        return modelMapper.map(egresosRepositorio.save(egresos), EgresoDTO.class);
    }

    public void eliminar(Long id) {
        egresosRepositorio.deleteById(id);
    }
}