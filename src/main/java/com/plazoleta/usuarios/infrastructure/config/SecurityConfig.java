package com.plazoleta.usuarios.infrastructure.config;

import com.plazoleta.usuarios.infrastructure.adapter.in.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private static final String ERROR_REQUEST_URI_ATTRIBUTE = "jakarta.servlet.error.request_uri";
	private static final String AUTH_ERROR_MESSAGE_ATTRIBUTE = "auth_error_message";
	private static final String DEFAULT_INVALID_TOKEN_MESSAGE = "Token invalido";
	private static final String APPLICATION_JSON = "application/json";

	private static final String LOGIN_PATH = "/usuarios/login";
	private static final String USER_ROLE_PATH = "/usuarios/*/rol";
	private static final String API_DOCS_PATH = "/v3/api-docs/**";
	private static final String SWAGGER_UI_PATH = "/swagger-ui/**";
	private static final String SWAGGER_UI_HTML_PATH = "/swagger-ui.html";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			String path = (String) request.getAttribute(ERROR_REQUEST_URI_ATTRIBUTE);
			if (path == null || path.isBlank()) {
				path = request.getRequestURI();
			}
			String message = (String) request.getAttribute(AUTH_ERROR_MESSAGE_ATTRIBUTE);
			if (message == null || message.isBlank()) {
				message = DEFAULT_INVALID_TOKEN_MESSAGE;
			}
			log.warn("Unauthorized request: path={}, message={}", path, authException.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(APPLICATION_JSON);
			response.getWriter().write("{\"message\":\"" + message + "\",\"code\":401,\"path\":\"" + path + "\"}");
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
			Authentication auth = org.springframework.security.core.context.SecurityContextHolder
					.getContext()
					.getAuthentication();
			String principal = auth == null ? "anonymous" : String.valueOf(auth.getPrincipal());
			String authorities = auth == null ? "[]" : auth.getAuthorities().toString();
			String path = request.getRequestURI();
			log.warn("Access denied: path={}, principal={}, authorities={}", path, principal, authorities);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		};

		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exceptions -> exceptions
						.accessDeniedHandler(accessDeniedHandler)
						.authenticationEntryPoint(authenticationEntryPoint()))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								LOGIN_PATH,
								USER_ROLE_PATH,
								API_DOCS_PATH,
								SWAGGER_UI_PATH,
								SWAGGER_UI_HTML_PATH)
						.permitAll()
						.anyRequest()
						.authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
