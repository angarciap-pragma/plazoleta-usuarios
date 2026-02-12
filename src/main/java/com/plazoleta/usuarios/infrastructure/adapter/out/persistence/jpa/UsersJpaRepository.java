package com.plazoleta.usuarios.infrastructure.adapter.out.persistence.jpa;

import java.util.Optional;

import com.plazoleta.usuarios.infrastructure.adapter.out.persistence.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<UsersEntity, Long> {
	Optional<UsersEntity> findByCorreo(String correo);

	boolean existsByCorreo(String correo);

	boolean existsByDocumentoIdentidad(String documentoIdentidad);
}
