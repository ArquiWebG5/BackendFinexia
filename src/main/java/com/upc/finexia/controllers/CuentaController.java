package com.upc.finexia.controllers;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public CuentaDTO insertar(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.insertar(cuentaDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CuentaDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return cuentaService.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    public CuentaDTO actualizar(@PathVariable Long id, @RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.actualizar(id, cuentaDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
    }

    // REPORTES
    //US32: Resumen financiero del usuario

    @GetMapping("/reporte/resumen/{usuarioId}")
    public ResponseEntity<List<ReporteResumenFinancieroDTO>> resumenFinanciero(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return new ResponseEntity<>(
                cuentaService.resumenFinanciero(usuarioId, desde, hasta),
                HttpStatus.OK);
    }

}