package com.plazoleta.usuarios.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.plazoleta.usuarios.application.port.in.CreateOwnerUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.exception.BadRequestException;
import com.plazoleta.usuarios.domain.exception.ConflictException;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.UsersModel;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CrearPropietarioServiceTest {

	@Test
	void crearPropietario_throwsWhenUnderage() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateOwnerService service = new CreateOwnerService(repository, encoder);

		// Comando con fecha menor de edad.
		LocalDate fechaNacimiento = LocalDate.now().minusYears(17);
		CreateOwnerUseCase.CreatePropietarioCommand command =
				new CreateOwnerUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createOwner(command))
				.isInstanceOf(BadRequestException.class);
	}

	@Test
	void crearPropietario_rejectsWhenCorreoExists() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateOwnerService service = new CreateOwnerService(repository, encoder);

		// El repositorio indica que el correo ya existe.
		when(repository.existsByEmail("ana@example.com")).thenReturn(true);

		// Comando valido, pero el correo ya esta registrado.
		LocalDate fechaNacimiento = LocalDate.now().minusYears(20);
		CreateOwnerUseCase.CreatePropietarioCommand command =
				new CreateOwnerUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createOwner(command))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void crearPropietario_rejectsWhenDocumentoIdentidadExists() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateOwnerService service = new CreateOwnerService(repository, encoder);

		// El correo no existe, pero el documento si.
		when(repository.existsByEmail("ana@example.com")).thenReturn(false);
		when(repository.existsByDocumentNumber("123")).thenReturn(true);
		// Comando valido, pero el documento ya esta registrado.
		LocalDate fechaNacimiento = LocalDate.now().minusYears(20);
		CreateOwnerUseCase.CreatePropietarioCommand command =
				new CreateOwnerUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createOwner(command))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void crearPropietario_createsOwner() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateOwnerService service = new CreateOwnerService(repository, encoder);

		// No existen duplicados y se simula el hash de la clave.
		when(repository.existsByEmail("ana@example.com")).thenReturn(false);
		when(repository.existsByDocumentNumber("123")).thenReturn(false);
		when(encoder.encode("secreto")).thenReturn("bcrypt-hash");
		LocalDate fechaNacimiento = LocalDate.now().minusYears(20);
		// Usuario esperado despues de guardar.
		UsersModel expected = new UsersModel(
				1L,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				fechaNacimiento,
				"ana@example.com",
				"bcrypt-hash",
				Role.PROPIETARIO);
		when(repository.save(any(UsersModel.class))).thenReturn(expected);

		// Comando valido para crear propietario.
		CreateOwnerUseCase.CreatePropietarioCommand command =
				new CreateOwnerUseCase.CreatePropietarioCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						fechaNacimiento,
						"ana@example.com",
						"secreto");

		// Llamada al caso de uso.
		UsersModel result = service.createOwner(command);

		// Asercion unica: el resultado debe coincidir.
		assertThat(result).isEqualTo(expected);
	}
}
