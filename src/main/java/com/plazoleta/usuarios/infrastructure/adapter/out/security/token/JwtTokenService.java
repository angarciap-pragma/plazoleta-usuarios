package com.plazoleta.usuarios.infrastructure.adapter.out.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.plazoleta.usuarios.application.port.out.TokenServicePort;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService implements TokenServicePort {

	private final SecretKey secretKey;
	private final long expirationMillis;

	public JwtTokenService(
			@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMillis = expirationSeconds * 1000L;
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

	@Override
	public String generateToken(String subject, Long userId, List<String> roles) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMillis);
		return Jwts.builder()
				.subject(subject)
				.claim("userId", userId)
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
