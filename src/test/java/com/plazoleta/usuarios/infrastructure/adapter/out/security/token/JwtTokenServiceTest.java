package com.plazoleta.usuarios.infrastructure.adapter.out.security.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class JwtTokenServiceTest {

	@Test
	void generateToken_thenParsesSubjectAndAuthorities() {
		// Servicio real a probar con secreto de prueba.
		JwtTokenService service = new JwtTokenService(
				"local-test-secret-2026-02-09-01-0123456789",
				3600);

		// Se genera un token con rol ADMIN.
		String token = service.generateToken("ana@example.com", 1L, List.of("ADMIN"));

		// Se valida el subject y las authorities derivadas del token.
		var subject = service.getSubject(token);
		var authorities = service.getAuthorities(token).stream()
				.map(granted -> granted.getAuthority())
				.toList();
		String result = subject + "|" + String.join(",", authorities);
		// Asercion unica: el resultado esperado debe coincidir.
		assertThat(result).isEqualTo("ana@example.com|ROLE_ADMIN");
	}
}
