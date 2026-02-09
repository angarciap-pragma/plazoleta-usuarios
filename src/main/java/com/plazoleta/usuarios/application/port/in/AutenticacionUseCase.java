package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.Role;

public interface AutenticacionUseCase {

	LoginResult login(LoginCommand command);

	record LoginCommand(String correo, String clave) {
	}

	record LoginResult(Long id, String correo, Role rol, String token) {
	}
}
