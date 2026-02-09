package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.Role;

public interface GetUsuarioRoleUseCase {

	Role obtenerRol(Long usuarioId);
}
