package com.plazoleta.usuarios.infrastructure.adapter.in.security;

import com.plazoleta.usuarios.infrastructure.adapter.out.security.token.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {	private static final String PREFIX_BEARER = "Bearer ";
	private static final String HEADER_AUTORIZACION = HttpHeaders.AUTHORIZATION;

	private static final String MENSAJE_TOKEN_EXPIRADO = "Token expirado";
	private static final String MENSAJE_TOKEN_INVALIDO = "Token invalido";

	private static final String LOG_TOKEN_EXPIRADO = "JWT expired";
	private static final String LOG_TOKEN_INVALIDO = "JWT authentication failed";
	private static final String LOG_TOKEN_AUTENTICADO = "JWT authenticated subject={} authorities={}";

	private final JwtTokenService jwtTokenService;
	private final AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader(HEADER_AUTORIZACION);
		if (header != null && header.startsWith(PREFIX_BEARER)) {
			String token = header.substring(PREFIX_BEARER.length());
			try {
				String subject = jwtTokenService.getSubject(token);
				var authorities = jwtTokenService.getAuthorities(token);
				var authentication = new UsernamePasswordAuthenticationToken(
						subject,
						null,
						authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug(LOG_TOKEN_AUTENTICADO, subject, authorities);
			} catch (ExpiredJwtException ex) {
				log.warn(LOG_TOKEN_EXPIRADO);
				SecurityContextHolder.clearContext();
				handleUnauthorized(request, response, MENSAJE_TOKEN_EXPIRADO, ex);
				return;
			} catch (JwtException | IllegalArgumentException ex) {
				log.warn(LOG_TOKEN_INVALIDO);
				SecurityContextHolder.clearContext();
				handleUnauthorized(request, response, MENSAJE_TOKEN_INVALIDO, ex);
				return;
			} catch (Exception ex) {
				log.error("Unexpected JWT error: {}", ex.getClass().getSimpleName(), ex);
				SecurityContextHolder.clearContext();
				handleUnauthorized(request, response, MENSAJE_TOKEN_INVALIDO, ex);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void handleUnauthorized(
			HttpServletRequest request,
			HttpServletResponse response,
			String message,
			Exception ex) throws IOException, ServletException {
		request.setAttribute("auth_error_message", message);
		authenticationEntryPoint.commence(
				request,
				response,
				new InsufficientAuthenticationException(message, ex));
	}
}