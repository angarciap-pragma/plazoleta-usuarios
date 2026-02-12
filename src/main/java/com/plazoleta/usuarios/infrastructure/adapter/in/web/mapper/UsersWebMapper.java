package com.plazoleta.usuarios.infrastructure.adapter.in.web.mapper;

import com.plazoleta.usuarios.application.port.in.AuthenticationUseCase;
import com.plazoleta.usuarios.application.port.in.CreateEmployeeUseCase;
import com.plazoleta.usuarios.application.port.in.CreateOwnerUseCase;
import com.plazoleta.usuarios.domain.model.Role;
import com.plazoleta.usuarios.domain.model.UsersModel;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.CreateEmployeeRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.CreateOwnerRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request.LoginRequest;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.LoginResponse;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.UsersResponse;
import com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.response.UsersRoleResponse;
import org.springframework.stereotype.Component;

@Component
public class UsersWebMapper {

	public AuthenticationUseCase.LoginCommand toLoginCommand(LoginRequest request) {
		return new AuthenticationUseCase.LoginCommand(request.getCorreo(), request.getClave());
	}

	public LoginResponse toLoginResponse(AuthenticationUseCase.LoginResult result) {
		return new LoginResponse(result.id(), result.email(), result.role(), result.token());
	}

	public CreateOwnerUseCase.CreatePropietarioCommand toCreatePropietarioCommand(
			CreateOwnerRequest request) {
		return new CreateOwnerUseCase.CreatePropietarioCommand(
				request.getNombre(),
				request.getApellido(),
				request.getDocumentoIdentidad(),
				request.getCelular(),
				request.getFechaNacimiento(),
				request.getCorreo(),
				request.getClave());
	}

	public CreateEmployeeUseCase.CreateEmployeeCommand toCreateEmpleadoCommand(
			CreateEmployeeRequest request) {
		return new CreateEmployeeUseCase.CreateEmployeeCommand(
				request.getNombre(),
				request.getApellido(),
				request.getDocumentoIdentidad(),
				request.getCelular(),
				request.getCorreo(),
				request.getClave(),
				request.getRol());
	}

	public UsersResponse toUserResponse(UsersModel user) {
		return new UsersResponse(
				user.id(),
				user.nombre(),
				user.apellido(),
				user.documentoIdentidad(),
				user.celular(),
				user.correo(),
				user.rol());
	}

	public UsersRoleResponse toUserRoleResponse(Long id, Role role) {
		return new UsersRoleResponse(id, role);
	}
}
