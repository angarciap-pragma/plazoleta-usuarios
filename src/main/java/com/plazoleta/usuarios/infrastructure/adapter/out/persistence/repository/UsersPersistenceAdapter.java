package com.plazoleta.usuarios.infrastructure.adapter.out.persistence.repository;

import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.model.UsersModel;
import com.plazoleta.usuarios.infrastructure.adapter.out.persistence.mapper.UsersPersistenceMapper;
import java.util.Optional;

import com.plazoleta.usuarios.infrastructure.adapter.out.persistence.jpa.UsersJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersPersistenceAdapter implements UsersRepositoryPort {

	private final UsersJpaRepository usersJpaRepository;
	private final UsersPersistenceMapper usersPersistenceMapper;

	@Override
	public UsersModel save(UsersModel usuario) {
		var entity = usersPersistenceMapper.toEntity(usuario);
		var saved = usersJpaRepository.save(entity);
		return usersPersistenceMapper.toDomain(saved);
	}

	@Override
	public Optional<UsersModel> findById(Long id) {
		return usersJpaRepository.findById(id)
				.map(usersPersistenceMapper::toDomain);
	}

	@Override
	public Optional<UsersModel> findByEmail(String correo) {
		return usersJpaRepository.findByCorreo(correo)
				.map(usersPersistenceMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String correo) {
		return usersJpaRepository.existsByCorreo(correo);
	}

	@Override
	public boolean existsByDocumentNumber(String documentoIdentidad) {
		return usersJpaRepository.existsByDocumentoIdentidad(documentoIdentidad);
	}
}
