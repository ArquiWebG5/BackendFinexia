package com.upc.finexia.security.services;

import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.security.dtos.RegistroUsuarioDTO;
import com.upc.finexia.security.entities.Role;
import com.upc.finexia.security.entities.User;
import com.upc.finexia.security.repositories.RoleRepository;
import com.upc.finexia.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Servicio de gestion de credenciales y roles de Spring Security.
// Cubre HU 01 (registro), HU 04 (actualizar datos), HU 05 (eliminar cuenta) y HU 19 (compartir acceso familiar).
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void grabar(Role role) {
        roleRepository.save(role);
    }

    // HU 19 - Compartir acceso familiar: asigna un rol adicional (tipicamente ROLE_USER) a un User existente.
    public Integer insertUserRol(Long user_id, Long rol_id) {
        userRepository.insertUserRol(user_id, rol_id);
        return 1;
    }

    // HU 01 - Registro de usuario: crea el perfil Usuario, codifica la contrasena y asigna rol ROLE_ADMIN
    // (responsable de finanzas). Devuelve el id del User creado para que el frontend pueda iniciar sesion.
    @Transactional
    public Long registrar(RegistroUsuarioDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPlan(dto.getPlan());
        usuario.setIdioma(dto.getIdioma());
        usuario.setMonedaPreferida(dto.getMonedaPreferida());
        usuario.setTemaUi(dto.getTemaUi());
        usuario.setFechaRegistro(LocalDate.now());
        usuario = usuarioRepositorio.save(usuario);

        Role rolAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN no configurado"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsuario(usuario);
        Set<Role> roles = new HashSet<>();
        roles.add(rolAdmin);
        user.setRoles(roles);

        user = userRepository.save(user);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        return user.getId();
    }
}
