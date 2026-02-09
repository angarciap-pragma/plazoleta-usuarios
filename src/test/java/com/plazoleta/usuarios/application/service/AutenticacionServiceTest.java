package com.plazoleta.usuarios.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.plazoleta.usuarios.application.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.application.port.in.AutenticacionUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsuarioRepositoryPort;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.security.JwtTokenService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AutenticacionServiceTest {

	@Test
	void login_returnsTokenWhenCredentialsValid() {
		UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		JwtTokenService jwtTokenService = mock(JwtTokenService.class);
		AutenticacionService service = new AutenticacionService(repository, encoder, jwtTokenService);

		Usuario usuario = new Usuario(
				1L,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				LocalDate.now().minusYears(20),
				"ana@example.com",
				"bcrypt",
				Role.ADMIN);

		when(repository.findByCorreo("ana@example.com")).thenReturn(Optional.of(usuario));
		when(encoder.matches("secreto", "bcrypt")).thenReturn(true);
		when(jwtTokenService.generateToken("ana@example.com", 1L, List.of("ADMIN")))
				.thenReturn("jwt-token");

		AutenticacionUseCase.LoginResult result = service.login(
				new AutenticacionUseCase.LoginCommand("ana@example.com", "secreto"));

		assertThat(result.token()).isEqualTo("jwt-token");
		assertThat(result.rol()).isEqualTo(Role.ADMIN);
	}

	@Test
	void login_throwsWhenCredentialsInvalid() {
		UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		JwtTokenService jwtTokenService = mock(JwtTokenService.class);
		AutenticacionService service = new AutenticacionService(repository, encoder, jwtTokenService);

		when(repository.findByCorreo("ana@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.login(
				new AutenticacionUseCase.LoginCommand("ana@example.com", "secreto")))
				.isInstanceOf(CredencialesInvalidasException.class);
	}
}
