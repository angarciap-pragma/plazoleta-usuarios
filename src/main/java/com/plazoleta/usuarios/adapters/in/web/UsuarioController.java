package com.plazoleta.usuarios.adapters.in.web;

import com.plazoleta.usuarios.adapters.in.web.dto.CreatePropietarioRequest;
import com.plazoleta.usuarios.adapters.in.web.dto.UsuarioResponse;
import com.plazoleta.usuarios.application.port.in.CreatePropietarioUseCase;
import com.plazoleta.usuarios.domain.model.Usuario;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/usuarios")
public class UsuarioController {

	private final CreatePropietarioUseCase createPropietarioUseCase;

	public UsuarioController(CreatePropietarioUseCase createPropietarioUseCase) {
		this.createPropietarioUseCase = createPropietarioUseCase;
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
}
