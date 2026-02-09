package com.plazoleta.usuarios.adapters.in.web;

import com.plazoleta.usuarios.application.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.application.exception.UsuarioNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		log.warn("Validation failed: {}", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		log.warn("Request rejected: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(UsuarioNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUsuarioNotFound(UsuarioNotFoundException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		log.warn("Usuario not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<Map<String, String>> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		log.warn("Login failed: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
}
