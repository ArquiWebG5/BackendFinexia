package com.upc.finexia.security.controllers;

import com.upc.finexia.security.dtos.AuthRequestDTO;
import com.upc.finexia.security.dtos.AuthResponseDTO;
import com.upc.finexia.security.dtos.RegistroUsuarioDTO;
import com.upc.finexia.security.services.CustomUserDetailsService;
import com.upc.finexia.security.services.UserService;
import com.upc.finexia.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

// Endpoints publicos de autenticacion.
// HU 01 - Registro de usuario  -> POST /api/registro
// HU 02 - Inicio de sesion    -> POST /api/authenticate
// HU 25 - Cerrar sesion       -> POST /api/logout
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    // HU 02 - Inicio de sesion: valida credenciales, genera JWT y devuelve roles.
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setRoles(roles);
        authResponseDTO.setJwt(token);
        return ResponseEntity.ok().headers(responseHeaders).body(authResponseDTO);
    }

    // HU 01 - Registro de usuario: endpoint publico que crea el User + Usuario y asigna el rol segun
    // el tipoUsuario elegido (RESPONSABLE -> ROLE_ADMIN, FAMILIAR -> ROLE_USER; por defecto RESPONSABLE).
    @PostMapping("/registro")
    public ResponseEntity<Long> registrar(@RequestBody RegistroUsuarioDTO dto) {
        Long userId = userService.registrar(dto);
        return ResponseEntity.ok(userId);
    }

    // HU 25 - Cerrar sesion: con JWT stateless el cliente descarta el token.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
