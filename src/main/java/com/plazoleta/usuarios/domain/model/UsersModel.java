package com.plazoleta.usuarios.domain.model;

import com.plazoleta.usuarios.domain.exception.BadRequestException;
import java.time.LocalDate;
import java.time.Period;

public record UsersModel(
		Long id,
		String nombre,
		String apellido,
		String documentoIdentidad,
		String celular,
		LocalDate fechaNacimiento,
		String correo,
		String clave,
		Role rol) {

	private static final int LEGAL_AGE = 18;
	private static final String USER_MUST_BE_ADULT = "El usuario debe ser mayor de edad";
	private static final String ROLE = "El rol debe ser: ";

	public static UsersModel createOwner(
			String nombre,
			String apellido,
			String documentoIdentidad,
			String celular,
			LocalDate fechaNacimiento,
			String correo,
			String claveCodificada) {
		if (!isAdult(fechaNacimiento)) {
			throw new BadRequestException(USER_MUST_BE_ADULT);
		}
		return new UsersModel(
				null,
				nombre,
				apellido,
				documentoIdentidad,
				celular,
				fechaNacimiento,
				correo,
				claveCodificada,
				Role.PROPIETARIO);
	}

	public static UsersModel createEmployee(
			String nombre,
			String apellido,
			String documentoIdentidad,
			String celular,
			String correo,
			String claveCodificada,
			String rol) {
		if (!Role.EMPLEADO.name().equalsIgnoreCase(rol)) {
			throw new BadRequestException(ROLE + Role.EMPLEADO.name());
		}
		return new UsersModel(
				null,
				nombre,
				apellido,
				documentoIdentidad,
				celular,
				null,
				correo,
				claveCodificada,
				Role.EMPLEADO);
	}

	private static boolean isAdult(LocalDate birthDate) {
		if (birthDate == null) {
			return false;
		}
		LocalDate today = LocalDate.now();
		return Period.between(birthDate, today).getYears() >= LEGAL_AGE;
	}
}
