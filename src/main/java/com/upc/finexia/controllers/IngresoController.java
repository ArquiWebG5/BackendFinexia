package com.upc.finexia.controllers;

import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.dtos.ReporteAnalisisAhorroDTO;
import com.upc.finexia.dtos.ReporteIngresosRecurrentesDTO;
import com.upc.finexia.services.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Controlador de Ingresos.
// HU 07 - Registrar ingreso   -> POST   /api/ingreso       (ADMIN)
// HU 08 - Visualizar ingresos -> GET    /api/ingresos/...  (lectura ADMIN/USER)
// HU 10 - Editar ingreso      -> PUT    /api/ingreso/{id}  (ADMIN)
// HU 12 - Eliminar ingreso    -> DELETE /api/ingreso/{id}  (ADMIN)
@RestController
@RequestMapping("/api")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    // HU 07 - Registrar ingreso.
    @PostMapping("/ingreso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngresoDTO> insertar(@RequestBody IngresoDTO ingresoDTO) {
        return ResponseEntity.ok(ingresoService.insertar(ingresoDTO));
    }

    // HU 08 - Visualizar ingresos registrados.
    @GetMapping("/ingresos/cuenta/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<IngresoDTO>> listarPorCuenta(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(ingresoService.listarPorCuenta(cuentaId));
    }

    @GetMapping("/ingresos/cuenta/{cuentaId}/categoria")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<IngresoDTO>> listarPorCategoria(@PathVariable Long cuentaId,
                                                               @RequestParam String categoria) {
        return ResponseEntity.ok(ingresoService.listarPorCategoria(cuentaId, categoria));
    }

    @GetMapping("/ingresos/cuenta/{cuentaId}/fechas")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<IngresoDTO>> listarPorFechas(@PathVariable Long cuentaId,
                                                            @RequestParam LocalDate desde,
                                                            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(ingresoService.listarPorFechas(cuentaId, desde, hasta));
    }

    // HU 10 - Editar ingreso.
    @PutMapping("/ingreso/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngresoDTO> actualizar(@PathVariable Long id, @RequestBody IngresoDTO ingresoDTO) {
        return ResponseEntity.ok(ingresoService.actualizar(id, ingresoDTO));
    }

    // HU 12 - Eliminar ingreso.
    @DeleteMapping("/ingreso/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        ingresoService.eliminar(id);
    }

    // Reporte de ingresos recurrentes (alimenta HU 30 - dashboard).
    @GetMapping("/reporte/recurrentes/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteIngresosRecurrentesDTO>> ingresosRecurrentes(
            @PathVariable Long cuentaId,
            @RequestParam(defaultValue = "2") int minMeses) {
        return new ResponseEntity<>(
                ingresoService.ingresosRecurrentes(cuentaId, minMeses),
                HttpStatus.OK);
    }

    // Analisis de ahorro potencial (alimenta HU 30 - dashboard).
    @GetMapping("/reporte/ahorro/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteAnalisisAhorroDTO>> analisisAhorro(@PathVariable Long cuentaId) {
        return new ResponseEntity<>(
                ingresoService.analisisAhorro(cuentaId),
                HttpStatus.OK);
    }
}
