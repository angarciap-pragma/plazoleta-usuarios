package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.Role;

public interface GetUsersRoleUseCase {

	Role getRole(Long userId);
}
