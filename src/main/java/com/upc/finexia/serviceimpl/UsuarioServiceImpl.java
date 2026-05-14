package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.security.repositories.UserRepository;
import com.upc.finexia.services.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Implementacion del servicio de Usuario.
// El registro (HU 01) se hace via AuthController -> UserService porque crea tambien credenciales.
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepositorio usuariosRepositorio;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    // HU 04 - Actualizar datos de usuario.
    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setPlan(usuarioDTO.getPlan());
        usuario.setIdioma(usuarioDTO.getIdioma());
        usuario.setMonedaPreferida(usuarioDTO.getMonedaPreferida());
        usuario.setTemaUi(usuarioDTO.getTemaUi());
        return modelMapper.map(usuariosRepositorio.save(usuario), UsuarioDTO.class);
    }

    // HU 05 - Eliminar cuenta: limpia roles (user_roles), elimina el User de seguridad, luego el Usuario.
    @Override
    @Transactional
    public void eliminar(Long id) {
        userRepository.findByUsuario_IdUsuario(id).ifPresent(user -> {
            user.getRoles().clear();
            userRepository.saveAndFlush(user);
            userRepository.delete(user);
        });
        usuariosRepositorio.deleteById(id);
    }

    @Override
    public List<UsuarioDTO> listar() {
        return usuariosRepositorio.findAll().stream()
                .map(u -> modelMapper.map(u, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    // HU 03 - Visualizar perfil de usuario.
    @Override
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}
