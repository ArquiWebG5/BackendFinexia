package com.upc.finexia.controllers;

import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.dtos.ReportePortafolioDTO;
import com.upc.finexia.dtos.ReporteTopPosicionesPortafolioDTO;
import com.upc.finexia.services.InversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // REPORTE

    //US27: Reporte de portafolio — distribución por tipo de activo
    @GetMapping("/reporte/portafolio/{usuarioId}")
    public ResponseEntity<List<ReportePortafolioDTO>> reportePortafolio(
            @PathVariable Long usuarioId) {
        return new ResponseEntity<>(
                inversionService.reportePortafolio(usuarioId),
                HttpStatus.OK);
    }

    //US27: Top posiciones del portafolio

    @GetMapping("/reporte/top/{usuarioId}")
    public ResponseEntity<List<ReporteTopPosicionesPortafolioDTO>> topPosicionesPortafolio(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "10") int top) {
        return new ResponseEntity<>(
                inversionService.topPosicionesPortafolio(usuarioId, top),
                HttpStatus.OK);
    }


}