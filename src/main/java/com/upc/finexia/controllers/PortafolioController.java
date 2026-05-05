package com.upc.finexia.controllers;

import com.upc.finexia.dtos.PortafolioDTO;
import com.upc.finexia.services.PortafolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portafolios")
public class PortafolioController {
    @Autowired
    private PortafolioService portafolioService;

    @PostMapping
    public PortafolioDTO insertar(@RequestBody PortafolioDTO dto) {
        return portafolioService.insertar(dto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<PortafolioDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return portafolioService.listarPorUsuario(usuarioId);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        portafolioService.eliminar(id);
    }
}