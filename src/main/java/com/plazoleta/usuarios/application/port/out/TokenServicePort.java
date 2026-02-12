package com.plazoleta.usuarios.application.port.out;

import java.util.List;

public interface TokenServicePort {

	String generateToken(String subject, Long userId, List<String> roles);
}
