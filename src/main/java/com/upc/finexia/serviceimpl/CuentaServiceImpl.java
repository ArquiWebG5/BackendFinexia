package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.ReportePatrimonioNetoDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresoRepositorio;
import com.upc.finexia.repositories.IngresoRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.repositories.VentaActivoRepositorio;
import com.upc.finexia.repositories.projections.ReporteResumenFinancieroProjection;
import com.upc.finexia.services.CuentaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private InversionRepositorio inversionRepositorio;

    @Autowired
    private VentaActivoRepositorio ventaActivoRepositorio;

    @Autowired
    private IngresoRepositorio ingresoRepositorio;

    @Autowired
    private EgresoRepositorio egresoRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CuentaDTO insertar(CuentaDTO cuentaDTO) {
        Usuario usuario = usuariosRepositorio.findById(cuentaDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
        cuenta.setUsuario(usuario);
        cuenta.setSaldoActual(cuentaDTO.getSaldoInicial());
        cuenta.setActiva(true);
        cuenta.setCreadoEn(LocalDate.now());
        return modelMapper.map(cuentaRepositorio.save(cuenta), CuentaDTO.class);
    }

    @Override
    public List<CuentaDTO> listarPorUsuario(Long usuarioId) {
        return cuentaRepositorio.findByUsuarioIdUsuario(usuarioId).stream()
                .map(c -> modelMapper.map(c, CuentaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO buscarPorId(Long id) {
        Cuenta cuenta = cuentaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return modelMapper.map(cuenta, CuentaDTO.class);
    }

    @Override
    public CuentaDTO actualizar(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setNombreCuenta(cuentaDTO.getNombreCuenta());
        cuenta.setBancoNombre(cuentaDTO.getBancoNombre());
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setMoneda(cuentaDTO.getMoneda());
        cuenta.setActiva(cuentaDTO.getActiva());
        return modelMapper.map(cuentaRepositorio.save(cuenta), CuentaDTO.class);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ventaActivoRepositorio.deleteByCuentaIdCuenta(id);
        ingresoRepositorio.deleteByCuentaIdCuenta(id);
        egresoRepositorio.deleteByCuentaIdCuenta(id);
        inversionRepositorio.deleteByCuentaIdCuenta(id);
        cuentaRepositorio.deleteById(id);
    }

    // HU 30 - Consultar dashboard: resumen financiero del hogar por cuenta.
    @Override
    public List<ReporteResumenFinancieroDTO> resumenFinanciero(Long usuarioId, LocalDate desde, LocalDate hasta) {
        return cuentaRepositorio.resumenFinancieroPorUsuario(
                        usuarioId,
                        desdeFiltro(desde),
                        hastaFiltro(hasta)
                ).stream()
                .map(this::toReporteResumenFinancieroDTO)
                .collect(Collectors.toList());
    }

    // HU 15 - Generar reporte de patrimonio neto.
    @Override
    public ReportePatrimonioNetoDTO patrimonioNeto(Long usuarioId) {
        Usuario usuario = usuariosRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        double totalCuentas = toDouble(cuentaRepositorio.saldoTotalPorUsuario(usuarioId));
        double totalInversiones = toDouble(inversionRepositorio.valorTotalPorUsuario(usuarioId));
        return new ReportePatrimonioNetoDTO(
                usuarioId,
                usuario.getMonedaPreferida(),
                totalCuentas,
                totalInversiones,
                totalCuentas + totalInversiones
        );
    }

    private ReporteResumenFinancieroDTO toReporteResumenFinancieroDTO(ReporteResumenFinancieroProjection reporte) {
        return new ReporteResumenFinancieroDTO(
                toLong(reporte.getCuentaId()),
                reporte.getNombreCuenta(),
                reporte.getBancoNombre(),
                reporte.getMoneda(),
                toDouble(reporte.getTotalIngresos()),
                toDouble(reporte.getTotalEgresos()),
                toDouble(reporte.getBalanceNeto()),
                toDouble(reporte.getSaldoActual())
        );
    }

    private Long toLong(Number value) {
        return value == null ? null : value.longValue();
    }

    private Double toDouble(Number value) {
        return value == null ? 0.0 : value.doubleValue();
    }

    private LocalDate desdeFiltro(LocalDate desde) {
        return desde == null ? LocalDate.of(1, 1, 1) : desde;
    }

    private LocalDate hastaFiltro(LocalDate hasta) {
        return hasta == null ? LocalDate.of(9999, 12, 31) : hasta;
    }
}
