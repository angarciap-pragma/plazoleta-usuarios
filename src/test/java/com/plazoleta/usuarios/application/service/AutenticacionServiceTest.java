package com.plazoleta.usuarios.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.plazoleta.usuarios.domain.exception.UnauthorizedException;
import com.plazoleta.usuarios.application.port.in.AuthenticationUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.TokenServicePort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.model.Role;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.plazoleta.usuarios.domain.model.UsersModel;
import org.junit.jupiter.api.Test;

class AutenticacionServiceTest {

	@Test
	void login_returnsTokenWhenCredentialsValid() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		TokenServicePort tokenService = mock(TokenServicePort.class);
		// Servicio real a probar.
		AuthenticationService service = new AuthenticationService(repository, encoder, tokenService);

		// Usuario de ejemplo que retornara el repositorio.
		UsersModel usuario = new UsersModel(
				1L,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				LocalDate.now().minusYears(20),
				"ana@example.com",
				"bcrypt",
				Role.ADMIN);

		// Comportamiento esperado de los mocks.
		when(repository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuario));
		when(encoder.matches("secreto", "bcrypt")).thenReturn(true);
		when(tokenService.generateToken("ana@example.com", 1L, List.of("ADMIN")))
				.thenReturn("jwt-token");

		// Llamada al caso de uso.
		AuthenticationUseCase.LoginResult result = service.login(
				new AuthenticationUseCase.LoginCommand("ana@example.com", "secreto"));

		// Asercion unica: el resultado debe ser el esperado.
		assertThat(result).isEqualTo(new AuthenticationUseCase.LoginResult(
				1L,
				"ana@example.com",
				Role.ADMIN,
				"jwt-token"));
	}

	@Test
	void login_throwsWhenCredentialsInvalid() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		TokenServicePort tokenService = mock(TokenServicePort.class);
		// Servicio real a probar.
		AuthenticationService service = new AuthenticationService(repository, encoder, tokenService);

		// No existe usuario con ese correo.
		when(repository.findByEmail("ana@example.com")).thenReturn(Optional.empty());

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.login(
				new AuthenticationUseCase.LoginCommand("ana@example.com", "secreto")))
				.isInstanceOf(UnauthorizedException.class);
	}

	@Test
	void login_throwsWhenPasswordDoesNotMatch() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		TokenServicePort tokenService = mock(TokenServicePort.class);
		// Servicio real a probar.
		AuthenticationService service = new AuthenticationService(repository, encoder, tokenService);

		// Usuario de ejemplo que retornara el repositorio.
		UsersModel usuario = new UsersModel(
				1L,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				LocalDate.now().minusYears(20),
				"ana@example.com",
				"bcrypt",
				Role.ADMIN);

		// El usuario existe, pero la contrasena no coincide.
		when(repository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuario));
		when(encoder.matches("secreto", "bcrypt")).thenReturn(false);

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.login(
				new AuthenticationUseCase.LoginCommand("ana@example.com", "secreto")))
				.isInstanceOf(UnauthorizedException.class);
	}
}
