package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.*;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Inversion;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import com.upc.finexia.services.InversionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InversionServiceImpl implements InversionService {

    @Autowired
    private InversionRepositorio inversionesRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    // HU 21 - Registrar inversion.
    @Override
    public InversionDTO insertar(InversionDTO inversionDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(inversionDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Inversion entidad = modelMapper.map(inversionDTO, Inversion.class);
        entidad.setCuenta(cuenta);
        if (entidad.getCreadoEn() == null) {
            entidad.setCreadoEn(LocalDate.now());
        }
        return modelMapper.map(inversionesRepositorio.save(entidad), InversionDTO.class);
    }

    // HU 24 - Listar inversiones.
    @Override
    public List<InversionDTO> listarPorCuenta(Long cuentaId) {
        return inversionesRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(i -> modelMapper.map(i, InversionDTO.class))
                .collect(Collectors.toList());
    }

    // HU 22 - Editar inversion.
    @Override
    public InversionDTO actualizar(Long id, InversionDTO inversionDTO) {
        Inversion entidad = inversionesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversion no encontrada"));
        entidad.setNombreActivo(inversionDTO.getNombreActivo());
        entidad.setTicker(inversionDTO.getTicker());
        entidad.setTipoActivo(inversionDTO.getTipoActivo());
        entidad.setBroker(inversionDTO.getBroker());
        entidad.setPrecioCompra(inversionDTO.getPrecioCompra());
        entidad.setCantidad(inversionDTO.getCantidad());
        entidad.setFechaCompra(inversionDTO.getFechaCompra());
        entidad.setValorTotal(inversionDTO.getValorTotal());
        entidad.setCategoria(inversionDTO.getCategoria());
        return modelMapper.map(inversionesRepositorio.save(entidad), InversionDTO.class);
    }

    // HU 23 - Eliminar inversion.
    @Override
    public void eliminar(Long id) {
        inversionesRepositorio.deleteById(id);
    }

    // HU 25 - Visualizar portafolio: distribucion por tipo de activo.
    @Override
    public List<ReportePortafolioDTO> reportePortafolio(Long usuarioId) {
        return inversionesRepositorio.reportePortafolio(usuarioId);
    }

    // HU 25 - Visualizar portafolio: top posiciones (holdings).
    @Override
    public List<ReporteTopPosicionesPortafolioDTO> topPosicionesPortafolio(Long usuarioId, int top) {
        return inversionesRepositorio.topPosicionesPortafolio(usuarioId).stream()
                .limit(top)
                .collect(Collectors.toList());
    }
}
