package com.upc.finexia.controllers;

import com.upc.finexia.dtos.*;
import com.upc.finexia.services.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EgresoController {

    @Autowired
    private EgresoService egresoService;

    @PostMapping("/egreso")
    public EgresoDTO insertar(@RequestBody EgresoDTO egresoDTO) {
        return egresoService.insertar(egresoDTO);
    }

    @GetMapping("/egresos/cuenta/{cuentaId}")
    public List<EgresoDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return egresoService.listarPorCuenta(cuentaId);
    }

    @GetMapping("/egresos/cuenta/{cuentaId}/categoria")
    public List<EgresoDTO> listarPorCategoria(@PathVariable Long cuentaId,
                                              @RequestParam String categoria) {
        return egresoService.listarPorCategoria(cuentaId, categoria);
    }

    @GetMapping("/egresos/cuenta/{cuentaId}/fechas")
    public List<EgresoDTO> listarPorFechas(@PathVariable Long cuentaId,
                                           @RequestParam LocalDate desde,
                                           @RequestParam LocalDate hasta) {
        return egresoService.listarPorFechas(cuentaId, desde, hasta);
    }

    @PutMapping("/egreso/{id}")
    public EgresoDTO actualizar(@PathVariable Long id, @RequestBody EgresoDTO egresoDTO) {
        return egresoService.actualizar(id, egresoDTO);
    }

    @DeleteMapping("/egreso/{id}")
    public void eliminar(@PathVariable Long id) {
        egresoService.eliminar(id);
    }

    // REPORTE
    //US28 & US37: Gastos por categoría

    @GetMapping("/reporte/por-categoria/{cuentaId}")
    public ResponseEntity<List<ReporteGastosPorCategoriaDTO>> gastosPorCategoria(
            @PathVariable Long cuentaId,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return new ResponseEntity<>(
                egresoService.gastosPorCategoria(cuentaId, desde, hasta),
                HttpStatus.OK);
    }

    //US29: Comparar gastos mensuales  FALTA CORREGIR

    @GetMapping("/reporte/mensuales/{cuentaId}")
    public ResponseEntity<List<ReporteGastosMensualesDTO>> gastosMensuales(
            @PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.gastosMensuales(cuentaId),
                HttpStatus.OK);
    }

    //US33: Detectar riesgos de gasto FALTA CORREGIR

    @GetMapping("/reporte/riesgos/{cuentaId}")
    public ResponseEntity<List<ReporteRiesgosGastoDTO>> detectarRiesgosGasto(
            @PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.detectarRiesgosGasto(cuentaId),
                HttpStatus.OK);
    }

    //US31: Top gastos del mes actual

    @GetMapping("/reporte/top/{cuentaId}")
    public ResponseEntity<List<ReporteTopGastosMesDTO>> topGastosMesActual(
            @PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.topGastosMesActual(cuentaId),
                HttpStatus.OK);
    }


}