package com.upc.finexia.services;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificacion;
import com.upc.finexia.entities.Usuarios;
import com.upc.finexia.repositories.NotificacionRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepositorio notificacionesRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public NotificacionDTO insertar(NotificacionDTO dto) {
        Usuarios usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Notificacion entidad = modelMapper.map(dto, Notificacion.class);
        entidad.setUsuario(usuario);
        return modelMapper.map(notificacionesRepositorio.save(entidad), NotificacionDTO.class);
    }

    public List<NotificacionDTO> listarPorUsuario(Long usuarioId) {
        return notificacionesRepositorio.findByUsuarioIdUsuario(usuarioId).stream() // ✅
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    public List<NotificacionDTO> listarNoLeidas(Long usuarioId) {
        return notificacionesRepositorio.findByUsuarioIdUsuarioAndLeido(usuarioId, false).stream() // ✅
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    public void marcarLeida(Long id) {
        Notificacion n = notificacionesRepositorio.findById(id).orElseThrow();
        n.setLeido(true);
        notificacionesRepositorio.save(n);
    }
}