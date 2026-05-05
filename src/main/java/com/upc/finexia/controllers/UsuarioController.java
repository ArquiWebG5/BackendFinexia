package com.upc.finexia.controllers;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public UsuarioDTO registrar(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.registrar(usuarioDTO);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizar(id, usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }
}