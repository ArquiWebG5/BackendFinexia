package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.*;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Egreso;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresoRepositorio;
import com.upc.finexia.services.EgresoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EgresoServiceImpl implements EgresoService {

    @Autowired
    private EgresoRepositorio egresosRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    // HU 06 - Registrar gasto.
    @Override
    @Transactional
    public EgresoDTO insertar(EgresoDTO egresoDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(egresoDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Egreso egreso = modelMapper.map(egresoDTO, Egreso.class);
        egreso.setCuenta(cuenta);
        egreso.setCreadoEn(LocalDate.now());
        cuenta.setSaldoActual(cuenta.getSaldoActual() - egresoDTO.getMonto());
        cuentaRepositorio.save(cuenta);
        return modelMapper.map(egresosRepositorio.save(egreso), EgresoDTO.class);
    }

    @Override
    public List<EgresoDTO> listarPorCuenta(Long cuentaId) {
        return egresosRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    // HU 13 - Clasificar gastos por categoria.
    @Override
    public List<EgresoDTO> listarPorCategoria(Long cuentaId, String categoria) {
        return egresosRepositorio.findByCuentaIdCuentaAndCategoria(cuentaId, categoria).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EgresoDTO> listarPorFechas(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresosRepositorio.findByCuentaIdCuentaAndFechaBetween(cuentaId, desde, hasta).stream()
                .map(e -> modelMapper.map(e, EgresoDTO.class))
                .collect(Collectors.toList());
    }

    // HU 09 - Editar gasto.
    @Override
    @Transactional
    public EgresoDTO actualizar(Long id, EgresoDTO egresoDTO) {
        Egreso egreso = egresosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));
        Cuenta cuenta = egreso.getCuenta();
        double montoAnterior = egreso.getMonto();
        egreso.setMonto(egresoDTO.getMonto());
        egreso.setFecha(egresoDTO.getFecha());
        egreso.setCategoria(egresoDTO.getCategoria());
        egreso.setNota(egresoDTO.getNota());
        egreso.setComprobante(egresoDTO.getComprobante());
        cuenta.setSaldoActual(cuenta.getSaldoActual() + montoAnterior - egresoDTO.getMonto());
        cuentaRepositorio.save(cuenta);
        return modelMapper.map(egresosRepositorio.save(egreso), EgresoDTO.class);
    }

    // HU 11 - Eliminar gasto.
    @Override
    @Transactional
    public void eliminar(Long id) {
        Egreso egreso = egresosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Egreso no encontrado"));
        Cuenta cuenta = egreso.getCuenta();
        cuenta.setSaldoActual(cuenta.getSaldoActual() + egreso.getMonto());
        egresosRepositorio.delete(egreso);
        cuentaRepositorio.save(cuenta);
    }

    // HU 13 / HU 26 - Reporte agregado por categoria.
    @Override
    public List<ReporteGastosPorCategoriaDTO> gastosPorCategoria(Long cuentaId, LocalDate desde, LocalDate hasta) {
        return egresosRepositorio.gastosPorCategoriaConFechas(
                cuentaId,
                desdeFiltro(desde),
                hastaFiltro(hasta)
        );
    }

    // HU 27 - Comparar gastos mensuales.
    @Override
    public List<ReporteGastosMensualesDTO> gastosMensuales(Long cuentaId) {
        return egresosRepositorio.gastosMensuales(cuentaId);
    }

    // Reporte de riesgos de sobregasto (uso interno, asociado a HU 26).
    @Override
    public List<ReporteRiesgosGastoDTO> detectarRiesgosGasto(Long cuentaId) {
        return egresosRepositorio.detectarRiesgosGasto(cuentaId);
    }

    // HU 26 - Top gastos del mes actual.
    @Override
    public List<ReporteTopGastosMesDTO> topGastosMesActual(Long cuentaId) {
        return egresosRepositorio.topGastosMesActual(cuentaId);
    }

    private LocalDate desdeFiltro(LocalDate desde) {
        return desde == null ? LocalDate.of(1, 1, 1) : desde;
    }

    private LocalDate hastaFiltro(LocalDate hasta) {
        return hasta == null ? LocalDate.of(9999, 12, 31) : hasta;
    }
}
