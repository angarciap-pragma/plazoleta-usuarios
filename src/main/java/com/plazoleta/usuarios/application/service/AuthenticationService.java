package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.domain.exception.UnauthorizedException;
import com.plazoleta.usuarios.application.port.in.AuthenticationUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.TokenServicePort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import java.util.List;

import com.plazoleta.usuarios.domain.model.UsersModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

	private final UsersRepositoryPort usuarioRepository;
	private final PasswordEncoderPort passwordEncoder;
	private final TokenServicePort tokenService;
	private static final String INVALID_CREDENCIAL = "Credenciales invalidas";

	@Override
	public LoginResult login(LoginCommand command) {
		log.info("Login request: email={}", command.email());
		UsersModel usuario = usuarioRepository.findByEmail(command.email())
				.orElseThrow(() -> new UnauthorizedException(INVALID_CREDENCIAL));

		if (!passwordEncoder.matches(command.password(), usuario.clave())) {
			log.warn("Login failed: email={}", command.password());
			throw new UnauthorizedException(INVALID_CREDENCIAL);
		}

		String token = tokenService.generateToken(
				usuario.correo(),
				usuario.id(),
				List.of(usuario.rol().name()));
		log.info("Login success: id={}, role={}", usuario.id(), usuario.rol());
		return new LoginResult(usuario.id(), usuario.correo(), usuario.rol(), token);
	}
}
