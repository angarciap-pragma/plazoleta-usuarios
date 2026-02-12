package com.plazoleta.usuarios.infrastructure.config;

import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.TokenServicePort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.application.service.AuthenticationService;
import com.plazoleta.usuarios.application.service.CreateEmployeeService;
import com.plazoleta.usuarios.application.service.CreateOwnerService;
import com.plazoleta.usuarios.application.service.GetUsersRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfig {

	@Bean
	public AuthenticationService autenticacionService(
			UsersRepositoryPort usuarioRepository,
			PasswordEncoderPort passwordEncoder,
			TokenServicePort tokenService) {
		return new AuthenticationService(usuarioRepository, passwordEncoder, tokenService);
	}

	@Bean
	public CreateEmployeeService createEmployeeService(
			UsersRepositoryPort usuarioRepository,
			PasswordEncoderPort passwordEncoder) {
		return new CreateEmployeeService(usuarioRepository, passwordEncoder);
	}

	@Bean
	public CreateOwnerService createOwnerService(
			UsersRepositoryPort usuarioRepository,
			PasswordEncoderPort passwordEncoder) {
		return new CreateOwnerService(usuarioRepository, passwordEncoder);
	}

	@Bean
	public GetUsersRoleService getUsersRoleService(
			UsersRepositoryPort usuarioRepository) {
		return new GetUsersRoleService(usuarioRepository);
	}
}
