package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.Role;

public interface AuthenticationUseCase {

	LoginResult login(LoginCommand command);

	record LoginCommand(String email, String password) {
	}

	record LoginResult(Long id, String email, Role role, String token) {
	}
}
