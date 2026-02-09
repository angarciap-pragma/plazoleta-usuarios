package com.plazoleta.usuarios.adapters.in.web;

import com.plazoleta.usuarios.adapters.in.web.dto.CreatePropietarioRequest;
import com.plazoleta.usuarios.adapters.in.web.dto.LoginRequest;
import com.plazoleta.usuarios.adapters.in.web.dto.LoginResponse;
import com.plazoleta.usuarios.adapters.in.web.dto.UsuarioResponse;
import com.plazoleta.usuarios.adapters.in.web.dto.UsuarioRoleResponse;
import com.plazoleta.usuarios.application.port.in.AutenticacionUseCase;
import com.plazoleta.usuarios.application.port.in.CreatePropietarioUseCase;
import com.plazoleta.usuarios.application.port.in.GetUsuarioRoleUseCase;
import com.plazoleta.usuarios.domain.model.Usuario;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/usuarios")
public class UsuarioController {

	private final CreatePropietarioUseCase createPropietarioUseCase;
	private final GetUsuarioRoleUseCase getUsuarioRoleUseCase;
	private final AutenticacionUseCase autenticacionUseCase;

	public UsuarioController(
			CreatePropietarioUseCase createPropietarioUseCase,
			GetUsuarioRoleUseCase getUsuarioRoleUseCase,
			AutenticacionUseCase autenticacionUseCase) {
		this.createPropietarioUseCase = createPropietarioUseCase;
		this.getUsuarioRoleUseCase = getUsuarioRoleUseCase;
		this.autenticacionUseCase = autenticacionUseCase;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		log.info("Login request: correo={}", request.getCorreo());
		var result = autenticacionUseCase.login(
				new AutenticacionUseCase.LoginCommand(request.getCorreo(), request.getClave()));
		log.info("Login success: id={}, rol={}", result.id(), result.rol());
		return ResponseEntity.ok(new LoginResponse(
				result.id(),
				result.correo(),
				result.rol(),
				result.token()));
	}

	@PostMapping("/propietarios")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioResponse> crearPropietario(
			@Valid @RequestBody CreatePropietarioRequest request) {
		log.info(
				"Create propietario request: documentoIdentidad={}, correo={}",
				request.getDocumentoIdentidad(),
				request.getCorreo());
		Usuario usuario = createPropietarioUseCase.crearPropietario(
				new CreatePropietarioUseCase.CreatePropietarioCommand(
						request.getNombre(),
						request.getApellido(),
						request.getDocumentoIdentidad(),
						request.getCelular(),
						request.getFechaNacimiento(),
						request.getCorreo(),
						request.getClave()));
		UsuarioResponse response = new UsuarioResponse(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getApellido(),
				usuario.getDocumentoIdentidad(),
				usuario.getCelular(),
				usuario.getFechaNacimiento(),
				usuario.getCorreo(),
				usuario.getRol());
		log.info("Create propietario success: id={}", usuario.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}/rol")
	public ResponseEntity<UsuarioRoleResponse> obtenerRol(@PathVariable("id") Long id) {
		log.info("Get role request: id={}", id);
		var rol = getUsuarioRoleUseCase.obtenerRol(id);
		return ResponseEntity.ok(new UsuarioRoleResponse(id, rol));
	}
}
