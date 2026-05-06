package com.upc.finexia.services;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Usuarios;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public CuentaDTO insertar(CuentaDTO cuentaDTO) {
        Usuarios usuario = usuariosRepositorio.findById(cuentaDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
        cuenta.setUsuario(usuario);
        cuenta.setSaldoActual(cuentaDTO.getSaldoInicial());
        cuenta.setActiva(true);
        cuenta.setCreadoEn(LocalDate.now());
        return modelMapper.map(cuentaRepositorio.save(cuenta), CuentaDTO.class);
    }

    public List<CuentaDTO> listarPorUsuario(Long usuarioId) {
        return cuentaRepositorio.findByUsuarioIdUsuario(usuarioId).stream()
                .map(c -> modelMapper.map(c, CuentaDTO.class))
                .collect(Collectors.toList());
    }

    public CuentaDTO buscarPorId(Long id) {
        Cuenta cuenta = cuentaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return modelMapper.map(cuenta, CuentaDTO.class);
    }

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

    public void eliminar(Long id) {
        cuentaRepositorio.deleteById(id);
    }

     //US32: Resumen financiero del usuario

    public List<ReporteResumenFinancieroDTO> resumenFinanciero(
            Long usuarioId, LocalDate desde, LocalDate hasta) {
        return cuentaRepositorio.resumenFinancieroPorUsuario(usuarioId, desde, hasta);
    }
}