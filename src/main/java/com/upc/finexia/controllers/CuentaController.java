package com.upc.finexia.controllers;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Controlador de Cuentas bancarias del usuario.
// HU 30 - Consultar dashboard -> GET /api/reporte/resumen/{usuarioId}
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    // Alta de cuenta (operacion del responsable de finanzas).
    @PostMapping("/cuenta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CuentaDTO> insertar(@RequestBody CuentaDTO cuentaDTO) {
        return ResponseEntity.ok(cuentaService.insertar(cuentaDTO));
    }

    // Cuentas del usuario (apoya HU 30 - dashboard, accesible tambien al familiar consulta).
    @GetMapping("/cuentas/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CuentaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(cuentaService.listarPorUsuario(usuarioId));
    }

    @PutMapping("/cuenta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CuentaDTO> actualizar(@PathVariable Long id, @RequestBody CuentaDTO cuentaDTO) {
        return ResponseEntity.ok(cuentaService.actualizar(id, cuentaDTO));
    }

    @DeleteMapping("/cuenta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
    }

    // HU 30 - Consultar dashboard: resumen financiero del hogar (lectura para familiar consulta).
    @GetMapping("/reporte/resumen/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteResumenFinancieroDTO>> resumenFinanciero(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return new ResponseEntity<>(
                cuentaService.resumenFinanciero(usuarioId, desde, hasta),
                HttpStatus.OK);
    }
}
