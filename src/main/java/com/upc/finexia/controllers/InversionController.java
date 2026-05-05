package com.upc.finexia.controllers;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.services.InversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inversiones")
public class InversionController {

    @Autowired
    private InversionService inversionService;

    @PostMapping
    public InversionDTO insertar(@RequestBody InversionDTO dto) {
        return inversionService.insertar(dto);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<InversionDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return inversionService.listarPorCuenta(cuentaId);
    }

    @PutMapping("/{id}")
    public InversionDTO actualizar(@PathVariable Long id, @RequestBody InversionDTO dto) {
        return inversionService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        inversionService.eliminar(id);
    }
}