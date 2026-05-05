package com.upc.finexia.services;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificacion;
import com.upc.finexia.repositories.NotificacionRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepositorio notificacionRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    public NotificacionDTO insertar(NotificacionDTO dto) {
        Notificacion entidad = modelMapper.map(dto, Notificacion.class);
        return modelMapper.map(notificacionRepositorio.save(entidad), NotificacionDTO.class);
    }

    public List<NotificacionDTO> listarPorUsuario(Long usuarioId) {
        return notificacionRepositorio.findByUsuarioId(usuarioId).stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    public List<NotificacionDTO> listarNoLeidas(Long usuarioId) {
        return notificacionRepositorio.findByUsuarioIdAndLeido(usuarioId, false).stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    public void marcarLeida(Long id) {
        Notificacion n = notificacionRepositorio.findById(id).orElseThrow();
        n.setLeido(true);
        notificacionRepositorio.save(n);
    }
}