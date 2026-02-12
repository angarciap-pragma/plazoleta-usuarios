package com.plazoleta.usuarios.infrastructure.adapter.in.web.controller;

import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.CreateEmployeeRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.CreateOwnerRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.LoginRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.LoginResponse;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.UsersResponse;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.UsersRoleResponse;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.mapper.UsersWebMapper;
import com.plazoleta.usuarios.application.port.in.AuthenticationUseCase;
import com.plazoleta.usuarios.application.port.in.CreateEmployeeUseCase;
import com.plazoleta.usuarios.application.port.in.CreateOwnerUseCase;
import com.plazoleta.usuarios.application.port.in.GetUsersRoleUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UsersController {

	private final CreateOwnerUseCase createOwnerUseCase;
	private final GetUsersRoleUseCase getUsersRoleUseCase;
	private final AuthenticationUseCase authenticationUseCase;
	private final CreateEmployeeUseCase createEmployeeUseCase;
	private final UsersWebMapper usersWebMapper;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		log.info("--- Start login ---");
		var result = authenticationUseCase.login(usersWebMapper.toLoginCommand(request));
		log.info("--- End login ---");
		return ResponseEntity.ok(usersWebMapper.toLoginResponse(result));
	}

	@PostMapping("/propietarios")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsersResponse> createOwner(@Valid @RequestBody CreateOwnerRequest request) {
		log.info("--- Start create owner ---");
		var usuario = createOwnerUseCase.createOwner(usersWebMapper.toCreatePropietarioCommand(request));
		UsersResponse response = usersWebMapper.toUserResponse(usuario);
		log.info("--- End create owner ---");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/empleados")
	@PreAuthorize("hasRole('PROPIETARIO')")
	public ResponseEntity<UsersResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
		log.info("--- Start create employee ---");
		var usuario = createEmployeeUseCase.createEmployee(usersWebMapper.toCreateEmpleadoCommand(request));
		UsersResponse response = usersWebMapper.toUserResponse(usuario);
		log.info("--- End create employee ---");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}/rol")
	public ResponseEntity<UsersRoleResponse> getRole(@PathVariable("id") Long id) {
		log.info("--- Start get role ---");
		var rol = getUsersRoleUseCase.getRole(id);
		log.info("--- End get role ---");
		return ResponseEntity.ok(usersWebMapper.toUserRoleResponse(id, rol));
	}
}
