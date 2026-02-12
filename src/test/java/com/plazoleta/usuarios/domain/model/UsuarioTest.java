package com.plazoleta.usuarios.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.plazoleta.usuarios.domain.exception.BadRequestException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UsuarioTest {

	@Test
	void nuevoPropietario_throwsWhenFechaNacimientoIsNull() {
		// Si la fecha es null, el dominio debe rechazar al propietario.
		assertThatThrownBy(() -> UsersModel.createOwner(
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				null,
				"ana@example.com",
				"bcrypt"))
				.isInstanceOf(BadRequestException.class);
	}

	@Test
	void nuevoPropietario_allowsWhenAgeIsEighteen() {
		// Se prepara una fecha que cumple la regla de mayoria de edad.
		LocalDate fechaNacimiento = LocalDate.now().minusYears(18);
		// Se crea el propietario con datos validos.
		UsersModel usuario = UsersModel.createOwner(
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				fechaNacimiento,
				"ana@example.com",
				"bcrypt");

		// Resultado esperado del factory de dominio.
		UsersModel expected = new UsersModel(
				null,
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				fechaNacimiento,
				"ana@example.com",
				"bcrypt",
				Role.PROPIETARIO);

		// Asercion unica: el objeto creado debe coincidir.
		assertThat(usuario).isEqualTo(expected);
	}

	@Test
	void nuevoEmpleado_throwsWhenRoleIsNotEmpleado() {
		// Si el rol no es EMPLEADO, el dominio debe rechazarlo.
		assertThatThrownBy(() -> UsersModel.createEmployee(
				"Ana",
				"Garcia",
				"123",
				"+573001112233",
				"ana@example.com",
				"bcrypt",
				"ADMIN"))
				.isInstanceOf(BadRequestException.class);
	}
}
