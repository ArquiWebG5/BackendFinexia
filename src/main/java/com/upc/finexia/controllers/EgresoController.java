package com.upc.finexia.controllers;

import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.services.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
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
}