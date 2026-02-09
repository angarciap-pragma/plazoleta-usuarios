package com.plazoleta.usuarios.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

	private final SecretKey secretKey;

	public JwtTokenService(@Value("${security.jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
		Claims claims = parseClaims(token);
		Object rolesClaim = claims.get("roles");
		if (rolesClaim instanceof List<?> roles) {
			return roles.stream()
					.filter(String.class::isInstance)
					.map(String.class::cast)
					.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
					.map(SimpleGrantedAuthority::new)
					.toList();
		}
		return List.of();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
