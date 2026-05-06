package com.upc.finexia.services;

import com.upc.finexia.dtos.*;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Inversion;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InversionService {

    @Autowired
    private InversionRepositorio inversionesRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public InversionDTO insertar(InversionDTO inversionDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(inversionDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Inversion entidad = modelMapper.map(inversionDTO, Inversion.class);
        entidad.setCuenta(cuenta);
        return modelMapper.map(inversionesRepositorio.save(entidad), InversionDTO.class);
    }

    public List<InversionDTO> listarPorCuenta(Long cuentaId) {
        return inversionesRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(i -> modelMapper.map(i, InversionDTO.class))
                .collect(Collectors.toList());
    }

    public InversionDTO actualizar(Long id, InversionDTO inversionDTO) {
        Inversion entidad = inversionesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversión no encontrada"));
        Inversion actualizada = inversionesRepositorio.save(entidad);
        return modelMapper.map(actualizada, InversionDTO.class);
    }

    public void eliminar(Long id) {
        inversionesRepositorio.deleteById(id);
    }

    //US27: Reporte de portafolio — distribución por tipo de activo

    public List<ReportePortafolioDTO> reportePortafolio(Long usuarioId) {
        return inversionesRepositorio.reportePortafolio(usuarioId);
    }

    //US27: Top posiciones del portafolio

    public List<ReporteTopPosicionesPortafolioDTO> topPosicionesPortafolio(
            Long usuarioId, int top) {
        List<ReporteTopPosicionesPortafolioDTO> todos =
                inversionesRepositorio.topPosicionesPortafolio(usuarioId);

        // Limitar a los top N resultados
        return todos.stream()
                .limit(top)
                .collect(Collectors.toList());
    }
}