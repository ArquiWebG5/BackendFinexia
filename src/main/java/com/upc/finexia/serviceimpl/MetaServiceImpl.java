package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.MetaDTO;
import com.upc.finexia.entities.Meta;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.MetaRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.services.MetaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaServiceImpl implements MetaService {

    @Autowired
    private MetaRepositorio metasRepositorio;

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    // HU 14 - Registrar meta de ahorro.
    @Override
    public MetaDTO insertar(MetaDTO dto) {
        Usuario usuario = usuariosRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Meta entidad = modelMapper.map(dto, Meta.class);
        entidad.setUsuario(usuario);
        if (entidad.getCreadoEn() == null) {
            entidad.setCreadoEn(LocalDate.now());
        }
        return modelMapper.map(metasRepositorio.save(entidad), MetaDTO.class);
    }

    // HU 18 - Listar metas de ahorro.
    @Override
    public List<MetaDTO> listarPorUsuario(Long usuarioId) {
        return metasRepositorio.findByUsuarioIdUsuario(usuarioId).stream()
                .map(m -> modelMapper.map(m, MetaDTO.class))
                .collect(Collectors.toList());
    }

    // HU 17 - Visualizar progreso de meta (devuelve la meta con progresoActual).
    @Override
    public MetaDTO buscarPorId(Long id) {
        Meta entidad = metasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        return modelMapper.map(entidad, MetaDTO.class);
    }

    // HU 15 - Editar meta de ahorro.
    @Override
    public MetaDTO actualizar(Long id, MetaDTO dto) {
        Meta entidad = metasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        entidad.setNombre(dto.getNombre());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setBalanceInicial(dto.getBalanceInicial());
        entidad.setContribucionMensual(dto.getContribucionMensual());
        entidad.setFechaObjetivo(dto.getFechaObjetivo());
        entidad.setProgresoActual(dto.getProgresoActual());
        entidad.setCategoria(dto.getCategoria());
        entidad.setEstado(dto.getEstado());
        return modelMapper.map(metasRepositorio.save(entidad), MetaDTO.class);
    }

    // HU 16 - Eliminar meta de ahorro.
    @Override
    public void eliminar(Long id) {
        metasRepositorio.deleteById(id);
    }
}
