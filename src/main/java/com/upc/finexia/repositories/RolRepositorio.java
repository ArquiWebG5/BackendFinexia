package com.upc.finexia.repositories;

import com.upc.finexia.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio del catalogo de roles de negocio.
// La autorizacion real (HU 02, HU 19) usa com.upc.finexia.security.entities.Role.
@Repository
public interface RolRepositorio extends JpaRepository<Rol, Long> {
}
