package com.plazoleta.usuarios.adapters.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
	Optional<UsuarioEntity> findByCorreo(String correo);
}
