package com.upc.finexia.controllers;

import com.upc.finexia.dtos.ActivoMercadoDTO;
import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.dtos.ReportePortafolioDTO;
import com.upc.finexia.dtos.ReporteTopPosicionesPortafolioDTO;
import com.upc.finexia.dtos.VentaActivoDTO;
import com.upc.finexia.services.InversionService;
import com.upc.finexia.services.MercadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador de Inversiones.
// HU 21 - Registrar inversion -> POST   /api/inversion        (ADMIN)
// HU 22 - Editar inversion    -> PUT    /api/inversion/{id}   (ADMIN)
// HU 23 - Eliminar inversion  -> DELETE /api/inversion/{id}   (ADMIN)
// HU 24 - Listar inversiones  -> GET    /api/inversiones/...  (lectura ADMIN/USER)
// HU 25 - Visualizar portafolio -> GET  /api/reporte/portafolio, /reporte/top-portafolio
// HU 10 - Registrar venta de activo -> POST /api/inversion/{id}/venta
@RestController
@RequestMapping("/api")
public class InversionController {

    @Autowired
    private InversionService inversionService;

    @Autowired
    private MercadoService mercadoService;

    // HU 21 - Registrar inversion.
    @PostMapping("/inversion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InversionDTO> insertar(@RequestBody InversionDTO dto) {
        return ResponseEntity.ok(inversionService.insertar(dto));
    }

    // HU 24 - Listar inversiones registradas.
    @GetMapping("/inversiones/cuenta/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<InversionDTO>> listarPorCuenta(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(inversionService.listarPorCuenta(cuentaId));
    }

    // HU 22 - Editar inversion.
    @PutMapping("/inversion/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InversionDTO> actualizar(@PathVariable Long id, @RequestBody InversionDTO dto) {
        return ResponseEntity.ok(inversionService.actualizar(id, dto));
    }

    // HU 23 - Eliminar inversion.
    @DeleteMapping("/inversion/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        inversionService.eliminar(id);
    }

    // HU 10 - Registrar venta de activo.
    @PostMapping("/inversion/{id}/venta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaActivoDTO> venderActivo(@PathVariable Long id, @RequestBody VentaActivoDTO dto) {
        return ResponseEntity.ok(inversionService.venderActivo(id, dto));
    }

    @GetMapping("/ventas-activo/cuenta/{cuentaId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<VentaActivoDTO>> listarVentasPorCuenta(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(inversionService.listarVentasPorCuenta(cuentaId));
    }

    // Busqueda de acciones/fondos con Finnhub.
    @GetMapping("/inversiones/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ActivoMercadoDTO>> buscarActivos(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(mercadoService.buscarActivos(q, limit));
    }

    @GetMapping("/inversiones/cotizacion/{symbol}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ActivoMercadoDTO> cotizacion(@PathVariable String symbol) {
        return ResponseEntity.ok(mercadoService.cotizacion(symbol));
    }

    // HU 25 - Visualizar portafolio: distribucion por tipo de activo.
    @GetMapping("/reporte/portafolio/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReportePortafolioDTO>> reportePortafolio(@PathVariable Long usuarioId) {
        return new ResponseEntity<>(
                inversionService.reportePortafolio(usuarioId),
                HttpStatus.OK);
    }

    // HU 25 - Visualizar portafolio: top posiciones (holdings).
    @GetMapping("/reporte/top-portafolio/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ReporteTopPosicionesPortafolioDTO>> topPosicionesPortafolio(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "10") int top) {
        return new ResponseEntity<>(
                inversionService.topPosicionesPortafolio(usuarioId, top),
                HttpStatus.OK);
    }
}
