package com.upc.finexia.controllers;

import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.dtos.ReporteAnalisisAhorroDTO;
import com.upc.finexia.dtos.ReporteIngresosRecurrentesDTO;
import com.upc.finexia.services.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    @PostMapping("/ingreso")
    public IngresoDTO insertar(@RequestBody IngresoDTO ingresoDTO) {
        return ingresoService.insertar(ingresoDTO);
    }

    @GetMapping("/ingresos/cuenta/{cuentaId}")
    public List<IngresoDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return ingresoService.listarPorCuenta(cuentaId);
    }

    @GetMapping("/ingresos/cuenta/{cuentaId}/categoria")
    public List<IngresoDTO> listarPorCategoria(@PathVariable Long cuentaId,
                                               @RequestParam String categoria) {
        return ingresoService.listarPorCategoria(cuentaId, categoria);
    }

    @GetMapping("/ingresos/cuenta/{cuentaId}/fechas")
    public List<IngresoDTO> listarPorFechas(@PathVariable Long cuentaId,
                                            @RequestParam LocalDate desde,
                                            @RequestParam LocalDate hasta) {
        return ingresoService.listarPorFechas(cuentaId, desde, hasta);
    }

    @PutMapping("/ingreso/{id}")
    public IngresoDTO actualizar(@PathVariable Long id, @RequestBody IngresoDTO ingresoDTO) {
        return ingresoService.actualizar(id, ingresoDTO);
    }

    @DeleteMapping("/ingreso/{id}")
    public void eliminar(@PathVariable Long id) {
        ingresoService.eliminar(id);
    }

    // REPORTE
    //US34: Ingresos recurrentes

    @GetMapping("/reporte/recurrentes/{cuentaId}")
    public ResponseEntity<List<ReporteIngresosRecurrentesDTO>> ingresosRecurrentes(
            @PathVariable Long cuentaId,
            @RequestParam(defaultValue = "2") int minMeses) {
        return new ResponseEntity<>(
                ingresoService.ingresosRecurrentes(cuentaId, minMeses),
                HttpStatus.OK);
    }

    //US30: Análisis de ahorro potencial


     @GetMapping("/reporte/ahorro/{cuentaId}")
    public ResponseEntity<List<ReporteAnalisisAhorroDTO>> analisisAhorro(
            @PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                ingresoService.analisisAhorro(cuentaId),
                HttpStatus.OK);
    }

}