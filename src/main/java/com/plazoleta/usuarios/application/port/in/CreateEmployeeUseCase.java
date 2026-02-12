package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.UsersModel;

public interface CreateEmployeeUseCase {

	UsersModel createEmployee(CreateEmployeeCommand command);

	record CreateEmployeeCommand(
			String nombre,
			String apellido,
			String documentoIdentidad,
			String celular,
			String correo,
			String clave,
			String rol) {
	}
}
