package com.upc.finexia.services;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificaciones;
import com.upc.finexia.entities.Usuarios;
import com.upc.finexia.repositories.NotificacionesRepositorio;
import com.upc.finexia.repositories.UsuariosRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionesRepositorio notificacionesRepositorio;

    @Autowired
    private UsuariosRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public NotificacionDTO insertar(NotificacionDTO dto) {
        Usuarios usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Notificaciones entidad = modelMapper.map(dto, Notificaciones.class);
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
        Notificaciones n = notificacionesRepositorio.findById(id).orElseThrow();
        n.setLeido(true);
        notificacionesRepositorio.save(n);
    }
}