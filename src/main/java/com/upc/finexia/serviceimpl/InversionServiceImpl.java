package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.*;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Inversion;
import com.upc.finexia.entities.VentaActivo;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import com.upc.finexia.repositories.VentaActivoRepositorio;
import com.upc.finexia.services.InversionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InversionServiceImpl implements InversionService {

    @Autowired
    private InversionRepositorio inversionesRepositorio;

    @Autowired
    private VentaActivoRepositorio ventaActivoRepositorio;

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
    @Transactional
    public void eliminar(Long id) {
        ventaActivoRepositorio.deleteByInversionId(id);
        inversionesRepositorio.deleteById(id);
    }

    // HU 10 - Registrar venta de activo.
    @Override
    @Transactional
    public VentaActivoDTO venderActivo(Long inversionId, VentaActivoDTO ventaDTO) {
        Inversion inversion = inversionesRepositorio.findById(inversionId)
                .orElseThrow(() -> new RuntimeException("Inversion no encontrada"));

        double cantidadDisponible = validarCantidadDisponible(inversion);
        double cantidadVendida = validarPositivo(ventaDTO.getCantidadVendida(), "La cantidad vendida debe ser mayor a cero");
        double precioVenta = validarPositivo(ventaDTO.getPrecioVenta(), "El precio de venta debe ser mayor a cero");

        if (cantidadVendida > cantidadDisponible) {
            throw new IllegalArgumentException("La cantidad vendida no puede superar la cantidad disponible");
        }

        double costoPromedio = costoPromedio(inversion, cantidadDisponible);
        double montoTotal = cantidadVendida * precioVenta;
        double costoTotal = cantidadVendida * costoPromedio;
        double cantidadRestante = cantidadDisponible - cantidadVendida;
        double valorRestante = Math.max(0.0, inversion.getValorTotal() - costoTotal);

        inversion.setCantidad(cantidadRestante);
        inversion.setValorTotal(valorRestante);
        inversionesRepositorio.save(inversion);

        VentaActivo venta = new VentaActivo();
        venta.setInversion(inversion);
        venta.setCuenta(inversion.getCuenta());
        venta.setNombreActivo(inversion.getNombreActivo());
        venta.setTicker(inversion.getTicker());
        venta.setCantidadVendida(cantidadVendida);
        venta.setPrecioVenta(precioVenta);
        venta.setMontoTotal(montoTotal);
        venta.setCostoPromedio(costoPromedio);
        venta.setGananciaPerdida(montoTotal - costoTotal);
        venta.setFechaVenta(ventaDTO.getFechaVenta() == null ? LocalDate.now() : ventaDTO.getFechaVenta());
        venta.setNota(ventaDTO.getNota());
        venta.setCreadoEn(LocalDate.now());

        VentaActivo ventaGuardada = ventaActivoRepositorio.save(venta);
        VentaActivoDTO respuesta = toVentaActivoDTO(ventaGuardada);
        respuesta.setCantidadRestante(cantidadRestante);
        respuesta.setValorRestante(valorRestante);
        return respuesta;
    }

    @Override
    public List<VentaActivoDTO> listarVentasPorCuenta(Long cuentaId) {
        return ventaActivoRepositorio.findByCuentaIdCuenta(cuentaId).stream()
                .map(this::toVentaActivoDTO)
                .collect(Collectors.toList());
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

    private double validarCantidadDisponible(Inversion inversion) {
        if (inversion.getCantidad() == null || inversion.getCantidad() <= 0) {
            throw new IllegalArgumentException("La inversion no tiene cantidad disponible para vender");
        }
        return inversion.getCantidad();
    }

    private double validarPositivo(Double value, String mensaje) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(mensaje);
        }
        return value;
    }

    private double costoPromedio(Inversion inversion, double cantidadDisponible) {
        if (inversion.getPrecioCompra() != null && inversion.getPrecioCompra() > 0) {
            return inversion.getPrecioCompra();
        }
        if (inversion.getValorTotal() > 0 && cantidadDisponible > 0) {
            return inversion.getValorTotal() / cantidadDisponible;
        }
        return 0.0;
    }

    private VentaActivoDTO toVentaActivoDTO(VentaActivo venta) {
        VentaActivoDTO dto = modelMapper.map(venta, VentaActivoDTO.class);
        dto.setInversionId(venta.getInversion().getId());
        dto.setCuentaId(venta.getCuenta().getIdCuenta());
        return dto;
    }
}
