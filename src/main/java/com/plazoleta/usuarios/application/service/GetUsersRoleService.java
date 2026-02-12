package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.application.port.in.GetUsersRoleUseCase;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.exception.NotFoundException;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.UsersModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GetUsersRoleService implements GetUsersRoleUseCase {

	private final UsersRepositoryPort usuarioRepository;
	private static final String USER_NOT_FOUND = "Usuario no encontrado: ";

	@Override
	public Role getRole(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.map(user -> {
					Role role = user.rol();
					log.info("User role found: id={}, role={}", usuarioId, role);
					return role;
				})
				.orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + usuarioId));
	}
}
