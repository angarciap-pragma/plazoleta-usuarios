package com.plazoleta.usuarios.application.service;

import com.plazoleta.usuarios.application.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.application.port.in.AutenticacionUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsuarioRepositoryPort;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.security.JwtTokenService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AutenticacionService implements AutenticacionUseCase {

	private final UsuarioRepositoryPort usuarioRepository;
	private final PasswordEncoderPort passwordEncoder;
	private final JwtTokenService jwtTokenService;

	public AutenticacionService(
			UsuarioRepositoryPort usuarioRepository,
			PasswordEncoderPort passwordEncoder,
			JwtTokenService jwtTokenService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public LoginResult login(LoginCommand command) {
		Usuario usuario = usuarioRepository.findByCorreo(command.correo())
				.orElseThrow(CredencialesInvalidasException::new);

		if (!passwordEncoder.matches(command.clave(), usuario.getClave())) {
			log.warn("Login failed: correo={}", command.correo());
			throw new CredencialesInvalidasException();
		}

		String token = jwtTokenService.generateToken(
				usuario.getCorreo(),
				usuario.getId(),
				List.of(usuario.getRol().name()));
		log.info("Login success: id={}, rol={}", usuario.getId(), usuario.getRol());
		return new LoginResult(usuario.getId(), usuario.getCorreo(), usuario.getRol(), token);
	}
}
