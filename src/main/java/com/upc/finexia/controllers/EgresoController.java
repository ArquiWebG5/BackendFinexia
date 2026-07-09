package com.upc.finexia.controllers;

import com.upc.finexia.dtos.*;
import com.upc.finexia.services.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Controlador de Egresos (gastos).
// HU 06 - Registrar gasto                 -> POST   /api/egreso              (ADMIN)
// HU 09 - Editar gasto                    -> PUT    /api/egreso/{id}         (ADMIN)
// HU 11 - Eliminar gasto                  -> DELETE /api/egreso/{id}         (ADMIN)
// HU 13 - Clasificar por categoria         -> GET    .../categoria + reporte (lectura ADMIN/USER)
// HU 26 - Reporte de gastos                -> GET    /api/reporte/por-categoria, /reporte/top
// HU 27 - Comparar gastos mensuales        -> GET    /api/reporte/mensuales/{cuentaId}
@RestController
@RequestMapping("/api")
public class EgresoController {

    @Autowired
    private EgresoService egresoService;

    // HU 06 - Registrar gasto.
    @PostMapping("/egreso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EgresoDTO> insertar(@RequestBody EgresoDTO egresoDTO) {
        return ResponseEntity.ok(egresoService.insertar(egresoDTO));
    }

    // Listado por cuenta (lectura general, util para HU 26).
    @GetMapping("/egresos/cuenta/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<EgresoDTO>> listarPorCuenta(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(egresoService.listarPorCuenta(cuentaId));
    }

    // HU 13 - Clasificar gastos por categoria (filtrado por categoria especifica).
    @GetMapping("/egresos/cuenta/{cuentaId}/categoria")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<EgresoDTO>> listarPorCategoria(@PathVariable Long cuentaId,
                                                              @RequestParam String categoria) {
        return ResponseEntity.ok(egresoService.listarPorCategoria(cuentaId, categoria));
    }

    @GetMapping("/egresos/cuenta/{cuentaId}/fechas")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<EgresoDTO>> listarPorFechas(@PathVariable Long cuentaId,
                                                           @RequestParam LocalDate desde,
                                                           @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(egresoService.listarPorFechas(cuentaId, desde, hasta));
    }

    // HU 09 - Editar gasto.
    @PutMapping("/egreso/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EgresoDTO> actualizar(@PathVariable Long id, @RequestBody EgresoDTO egresoDTO) {
        return ResponseEntity.ok(egresoService.actualizar(id, egresoDTO));
    }

    // HU 11 - Eliminar gasto.
    @DeleteMapping("/egreso/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        egresoService.eliminar(id);
    }

    // HU 13 / HU 26 - Reporte de gastos por categoria.
    @GetMapping("/reporte/por-categoria/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteGastosPorCategoriaDTO>> gastosPorCategoria(
            @PathVariable Long cuentaId,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return new ResponseEntity<>(
                egresoService.gastosPorCategoria(cuentaId, desde, hasta),
                HttpStatus.OK);
    }

    // HU 27 - Comparar gastos mensuales.
    @GetMapping("/reporte/mensuales/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteGastosMensualesDTO>> gastosMensuales(@PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.gastosMensuales(cuentaId),
                HttpStatus.OK);
    }

    // Reporte de riesgos de sobregasto (alimenta HU 26 / HU 30 - dashboard).
    @GetMapping("/reporte/riesgos/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteRiesgosGastoDTO>> detectarRiesgosGasto(@PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.detectarRiesgosGasto(cuentaId),
                HttpStatus.OK);
    }

    // HU 26 - Top gastos del mes actual.
    @GetMapping("/reporte/top/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteTopGastosMesDTO>> topGastosMesActual(@PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                egresoService.topGastosMesActual(cuentaId),
                HttpStatus.OK);
    }
}
