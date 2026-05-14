package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.NotificacionDTO;
import com.upc.finexia.entities.Notificacion;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.NotificacionRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.services.NotificacionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepositorio notificacionesRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    // HU 29 - Crear una notificacion para un usuario (ej. al registrar un gasto o cumplir una meta).
    @Override
    public NotificacionDTO insertar(NotificacionDTO dto) {
        Usuario usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Notificacion entidad = modelMapper.map(dto, Notificacion.class);
        entidad.setUsuario(usuario);
        if (entidad.getLeido() == null) {
            entidad.setLeido(false);
        }
        if (entidad.getCreadoEn() == null) {
            entidad.setCreadoEn(LocalDate.now());
        }
        return modelMapper.map(notificacionesRepositorio.save(entidad), NotificacionDTO.class);
    }

    // HU 29 - Listado completo de notificaciones del usuario.
    @Override
    public List<NotificacionDTO> listarPorUsuario(Long usuarioId) {
        return notificacionesRepositorio.findByUsuarioIdUsuario(usuarioId).stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    // HU 29 - Bandeja de no leidas.
    @Override
    public List<NotificacionDTO> listarNoLeidas(Long usuarioId) {
        return notificacionesRepositorio.findByUsuarioIdUsuarioAndLeido(usuarioId, false).stream()
                .map(n -> modelMapper.map(n, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    // HU 29 - Marcar una notificacion como leida.
    @Override
    public void marcarLeida(Long id) {
        Notificacion n = notificacionesRepositorio.findById(id).orElseThrow();
        n.setLeido(true);
        notificacionesRepositorio.save(n);
    }
}
