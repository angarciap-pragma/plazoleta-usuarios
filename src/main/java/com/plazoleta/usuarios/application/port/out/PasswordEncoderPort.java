package com.plazoleta.usuarios.application.port.out;

public interface PasswordEncoderPort {

	String encode(String rawPassword);
}
