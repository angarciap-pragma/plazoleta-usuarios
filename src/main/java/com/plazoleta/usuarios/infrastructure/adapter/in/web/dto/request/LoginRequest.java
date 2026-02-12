package com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

	@NotBlank
	@Email
	private String correo;

	@NotBlank
	private String clave;

}
