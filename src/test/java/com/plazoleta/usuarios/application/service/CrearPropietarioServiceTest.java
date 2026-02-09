package com.plazoleta.usuarios.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.plazoleta.usuarios.application.port.in.CreatePropietarioUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsuarioRepositoryPort;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.Usuario;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CrearPropietarioServiceTest {

	@Test
	void crearPropietario_throwsWhenUnderage() {
		UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		CrearPropietarioService service = new CrearPropietarioService(repository, encoder);

		LocalDate fechaNacimiento = LocalDate.now().minusYears(17);
		CreatePropietarioUseCase.CreatePropietarioCommand command =
				new CreatePropietarioUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		assertThatThrownBy(() -> service.crearPropietario(command))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("El usuario debe ser mayor de edad");
	}

	@Test
	void crearPropietario_createsOwner() {
		UsuarioRepositoryPort repository = mock(UsuarioRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		CrearPropietarioService service = new CrearPropietarioService(repository, encoder);

		when(encoder.encode("secreto")).thenReturn("bcrypt-hash");
		when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
			Usuario usuario = invocation.getArgument(0);
			return new Usuario(
					1L,
					usuario.getNombre(),
					usuario.getApellido(),
					usuario.getDocumentoIdentidad(),
					usuario.getCelular(),
					usuario.getFechaNacimiento(),
					usuario.getCorreo(),
					usuario.getClave(),
					usuario.getRol());
		});

		LocalDate fechaNacimiento = LocalDate.now().minusYears(20);
		CreatePropietarioUseCase.CreatePropietarioCommand command =
				new CreatePropietarioUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		Usuario result = service.crearPropietario(command);

		verify(encoder).encode("secreto");
		verify(repository).save(any(Usuario.class));
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getRol()).isEqualTo(Role.PROPIETARIO);
		assertThat(result.getClave()).isEqualTo("bcrypt-hash");
	}
}
