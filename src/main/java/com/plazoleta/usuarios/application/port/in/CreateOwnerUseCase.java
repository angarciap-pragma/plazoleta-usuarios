package com.plazoleta.usuarios.application.port.in;

import com.plazoleta.usuarios.domain.model.UsersModel;
import java.time.LocalDate;

public interface CreateOwnerUseCase {

	UsersModel createOwner(CreatePropietarioCommand command);

	record CreatePropietarioCommand(
			String nombre,
			String apellido,
			String documentoIdentidad,
			String celular,
			LocalDate fechaNacimiento,
			String correo,
			String clave) {
	}
}
