package com.upc.finexia.controllers;

import com.upc.finexia.dtos.PortafolioDTO;
import com.upc.finexia.services.PortafolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador de snapshots de Portafolio.
// HU 25 - Visualizar portafolio (snapshot agregado del hogar).
@RestController
@RequestMapping("/api")
public class PortafolioController {

    @Autowired
    private PortafolioService portafolioService;

    @PostMapping("/portafolio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PortafolioDTO> insertar(@RequestBody PortafolioDTO dto) {
        return ResponseEntity.ok(portafolioService.insertar(dto));
    }

    // HU 25 - Visualizar portafolio del hogar.
    @GetMapping("/portafolios/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<PortafolioDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(portafolioService.listarPorUsuario(usuarioId));
    }

    @DeleteMapping("/portafolio/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        portafolioService.eliminar(id);
    }
}
