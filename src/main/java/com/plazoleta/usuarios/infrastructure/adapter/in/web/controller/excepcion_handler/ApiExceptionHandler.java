package com.plazoleta.usuarios.infrastructure.adapter.in.web.controller.excepcion_handler;

import com.plazoleta.usuarios.domain.exception.ApiException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
	private static final String MESSAGE_KEY = "message";
	private static final String CODE_KEY = "code";
	private static final String DETAILS_KEY = "details";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String PATH_KEY = "path";

	private static final String VALIDATION_FAILED = "Validación fallida";
	private static final String INVALID_JSON = "JSON inválido en la solicitud";
	private static final String NOT_FOUND = "Ruta no encontrada";
	private static final String UNEXPECTED_ERROR = "Error interno del servidor";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String> fieldErrors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.put(error.getField(), error.getDefaultMessage());
		}
		log.warn("Validation failed: {}", fieldErrors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorWithDetails(VALIDATION_FAILED, HttpStatus.BAD_REQUEST, request, fieldErrors));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(
			IllegalArgumentException ex,
			HttpServletRequest request) {
		log.warn("Request rejected: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorOnly(ex.getMessage(), HttpStatus.BAD_REQUEST, request));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleNotReadable(
			HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		log.warn("Invalid JSON request");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorOnly(INVALID_JSON, HttpStatus.BAD_REQUEST, request));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNoResource(
			NoResourceFoundException ex,
			HttpServletRequest request) {
		log.warn("No resource found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(errorOnly(NOT_FOUND, HttpStatus.NOT_FOUND, request));
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Map<String, Object>> handleApiException(
			ApiException ex,
			HttpServletRequest request) {
		log.warn("Request failed: {}", ex.getMessage());
		return ResponseEntity.status(ex.getStatus())
				.body(errorOnly(ex.getMessage(), ex.getStatus(), request));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleUnexpected(
			Exception ex,
			HttpServletRequest request) {
		log.error("Unexpected error: {}", ex.getClass().getSimpleName(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(errorOnly(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request));
	}

	private Map<String, Object> errorOnly(String message, HttpStatus status, HttpServletRequest request) {
		return baseBody(message, status, request);
	}

	private Map<String, Object> errorWithDetails(
			String message,
			HttpStatus status,
			HttpServletRequest request,
			Map<String, String> details) {
		Map<String, Object> body = baseBody(message, status, request);
		body.put(DETAILS_KEY, details);
		return body;
	}

	private Map<String, Object> baseBody(String message, HttpStatus status, HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(MESSAGE_KEY, message);
		body.put(CODE_KEY, status.value());
		body.put(TIMESTAMP_KEY, OffsetDateTime.now().toString());
		body.put(PATH_KEY, request.getRequestURI());
		return body;
	}
}