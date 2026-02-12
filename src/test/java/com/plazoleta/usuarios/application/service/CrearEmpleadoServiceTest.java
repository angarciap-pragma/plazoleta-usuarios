package com.plazoleta.usuarios.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.plazoleta.usuarios.application.port.in.CreateEmployeeUseCase;
import com.plazoleta.usuarios.application.port.out.PasswordEncoderPort;
import com.plazoleta.usuarios.application.port.out.UsersRepositoryPort;
import com.plazoleta.usuarios.domain.exception.BadRequestException;
import com.plazoleta.usuarios.domain.exception.ConflictException;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.UsersModel;
import org.junit.jupiter.api.Test;

class CrearEmpleadoServiceTest {

	@Test
	void crearEmpleado_rejectsWhenRoleIsNotEmpleado() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateEmployeeService service = new CreateEmployeeService(repository, encoder);

		// Comando con rol invalido.
		CreateEmployeeUseCase.CreateEmployeeCommand command =
				new CreateEmployeeUseCase.CreateEmployeeCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						"ana@example.com",
						"secreto",
						"ADMIN");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createEmployee(command))
				.isInstanceOf(BadRequestException.class);
	}

	@Test
	void crearEmpleado_rejectsWhenCorreoExists() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateEmployeeService service = new CreateEmployeeService(repository, encoder);

		// El repositorio indica que el correo ya existe.
		when(repository.existsByEmail("ana@example.com")).thenReturn(true);

		// Comando valido, pero el correo ya esta registrado.
		CreateEmployeeUseCase.CreateEmployeeCommand command =
				new CreateEmployeeUseCase.CreateEmployeeCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						"ana@example.com",
						"secreto",
						"EMPLEADO");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createEmployee(command))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void crearEmpleado_rejectsWhenDocumentoIdentidadExists() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateEmployeeService service = new CreateEmployeeService(repository, encoder);

		// El correo no existe, pero el documento si.
		when(repository.existsByEmail("ana@example.com")).thenReturn(false);
		when(repository.existsByDocumentNumber("123")).thenReturn(true);

		// Comando valido, pero el documento ya esta registrado.
		CreateEmployeeUseCase.CreateEmployeeCommand command =
				new CreateEmployeeUseCase.CreateEmployeeCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						"ana@example.com",
						"secreto",
						"EMPLEADO");

		// Asercion unica: debe lanzar excepcion.
		assertThatThrownBy(() -> service.createEmployee(command))
				.isInstanceOf(ConflictException.class);
	}

	@Test
	void crearEmpleado_createsEmployee() {
		// Mocks para aislar la prueba de infraestructura.
		UsersRepositoryPort repository = mock(UsersRepositoryPort.class);
		PasswordEncoderPort encoder = mock(PasswordEncoderPort.class);
		// Servicio real a probar.
		CreateEmployeeService service = new CreateEmployeeService(repository, encoder);

		// No existen duplicados y se simula el hash de la clave.
		when(repository.existsByEmail("ana@example.com")).thenReturn(false);
		when(repository.existsByDocumentNumber("123")).thenReturn(false);
		when(encoder.encode("secreto")).thenReturn("bcrypt-hash");
		// Usuario esperado despues de guardar.
		UsersModel expected = new UsersModel(
				1L,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				null,
				"ana@example.com",
				"bcrypt-hash",
				Role.EMPLEADO);
		when(repository.save(any(UsersModel.class))).thenReturn(expected);

		// Comando valido para crear empleado.
		CreateEmployeeUseCase.CreateEmployeeCommand command =
				new CreateEmployeeUseCase.CreateEmployeeCommand(
						"Ana",
						"Garcia",
						"123",
						"+573001112233",
						"ana@example.com",
						"secreto",
						"EMPLEADO");

		// Llamada al caso de uso.
		UsersModel result = service.createEmployee(command);

		// Asercion unica: el resultado debe coincidir.
		assertThat(result).isEqualTo(expected);
	}
}
