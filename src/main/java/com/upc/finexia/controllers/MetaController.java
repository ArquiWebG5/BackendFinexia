package com.upc.finexia.controllers;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.services.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador de Metas de ahorro.
// HU 14 - Registrar meta            -> POST   /api/meta             (ADMIN)
// HU 15 - Editar meta               -> PUT    /api/meta/{id}        (ADMIN)
// HU 16 - Eliminar meta             -> DELETE /api/meta/{id}        (ADMIN)
// HU 17 - Visualizar progreso meta  -> GET    /api/meta/{id}        (lectura ADMIN/USER)
// HU 18 - Listar metas              -> GET    /api/metas/usuario/{usuarioId} (lectura ADMIN/USER)
@RestController
@RequestMapping("/api")
public class MetaController {

    @Autowired
    private MetaService metaService;

    // HU 14 - Registrar meta de ahorro.
    @PostMapping("/meta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MetaDTO> insertar(@RequestBody MetaDTO dto) {
        return ResponseEntity.ok(metaService.insertar(dto));
    }

    // HU 18 - Listar metas de ahorro registradas.
    @GetMapping("/metas/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<MetaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(metaService.listarPorUsuario(usuarioId));
    }

    // HU 17 - Visualizar progreso de una meta especifica.
    @GetMapping("/meta/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<MetaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(metaService.buscarPorId(id));
    }

    // HU 15 - Editar meta de ahorro.
    @PutMapping("/meta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MetaDTO> actualizar(@PathVariable Long id, @RequestBody MetaDTO dto) {
        return ResponseEntity.ok(metaService.actualizar(id, dto));
    }

    // HU 16 - Eliminar meta de ahorro.
    @DeleteMapping("/meta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        metaService.eliminar(id);
    }
}
