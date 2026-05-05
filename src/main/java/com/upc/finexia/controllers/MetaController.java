package com.upc.finexia.controllers;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.services.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
public class MetaController {
    @Autowired
    private MetaService metaService;

    @PostMapping
    public MetaDTO insertar(@RequestBody MetaDTO dto) {
        return metaService.insertar(dto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<MetaDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return metaService.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    public MetaDTO actualizar(@PathVariable Long id, @RequestBody MetaDTO dto) {
        return metaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        metaService.eliminar(id);
    }
}