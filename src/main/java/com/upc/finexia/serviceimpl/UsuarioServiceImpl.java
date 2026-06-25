package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.UsuarioDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresoRepositorio;
import com.upc.finexia.repositories.IngresoRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import com.upc.finexia.repositories.MetaRepositorio;
import com.upc.finexia.repositories.NotificacionRepositorio;
import com.upc.finexia.repositories.PortafolioRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.repositories.VentaActivoRepositorio;
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
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private IngresoRepositorio ingresoRepositorio;

    @Autowired
    private EgresoRepositorio egresoRepositorio;

    @Autowired
    private InversionRepositorio inversionRepositorio;

    @Autowired
    private MetaRepositorio metaRepositorio;

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    @Autowired
    private PortafolioRepositorio portafolioRepositorio;

    @Autowired
    private VentaActivoRepositorio ventaActivoRepositorio;

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
        Usuario usuario = usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Long> cuentaIds = cuentaRepositorio.findByUsuarioIdUsuario(id).stream()
                .map(Cuenta::getIdCuenta)
                .collect(Collectors.toList());

        cuentaIds.forEach(cuentaId -> {
            ventaActivoRepositorio.deleteByCuentaIdCuenta(cuentaId);
            ingresoRepositorio.deleteByCuentaIdCuenta(cuentaId);
            egresoRepositorio.deleteByCuentaIdCuenta(cuentaId);
            inversionRepositorio.deleteByCuentaIdCuenta(cuentaId);
        });
        cuentaRepositorio.deleteByUsuarioIdUsuario(id);
        metaRepositorio.deleteByUsuarioIdUsuario(id);
        notificacionRepositorio.deleteByUsuarioIdUsuario(id);
        portafolioRepositorio.deleteByUsuarioIdUsuario(id);

        userRepository.findByUsuario_IdUsuario(id).ifPresent(user -> {
            user.getRoles().clear();
            user.setUsuario(null);
            userRepository.saveAndFlush(user);
            userRepository.delete(user);
        });
        usuariosRepositorio.delete(usuario);
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

    // Resuelve el perfil del usuario autenticado (login del frontend): username -> User -> Usuario.
    @Override
    public UsuarioDTO buscarPorUsername(String username) {
        Usuario usuario = userRepository.findByUsername(username)
                .map(user -> user.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}
