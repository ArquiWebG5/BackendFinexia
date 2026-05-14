package com.upc.finexia.repositories;

import com.upc.finexia.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio del perfil de Usuario (HU 03 - perfil, HU 04 - actualizar, HU 05 - eliminar).
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
