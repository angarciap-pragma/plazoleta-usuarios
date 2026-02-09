package com.plazoleta.usuarios.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenService jwtTokenService;

	public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				String subject = jwtTokenService.getSubject(token);
				var authorities = jwtTokenService.getAuthorities(token);
				var authentication = new UsernamePasswordAuthenticationToken(
						subject,
						null,
						authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("JWT authenticated subject={} authorities={}", subject, authorities);
			} catch (Exception ex) {
				log.warn("JWT authentication failed: {}", ex.getClass().getSimpleName());
				SecurityContextHolder.clearContext();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
