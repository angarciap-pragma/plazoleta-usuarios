package com.plazoleta.usuarios.application.exception;

public class CredencialesInvalidasException extends RuntimeException {

	public CredencialesInvalidasException() {
		super("Credenciales invalidas");
	}
}
