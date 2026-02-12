package com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateEmployeeRequest {

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellido;

	@NotBlank
	@Pattern(regexp = "\\d+", message = "El documento de identidad debe ser numerico")
	private String documentoIdentidad;

	@NotBlank
	@Size(max = 13)
	@Pattern(regexp = "^(\\+)?\\d+$", message = "El celular debe contener solo numeros y puede iniciar con +")
	private String celular;

	@NotBlank
	@Email
	private String correo;

	@NotBlank
	private String clave;

	@NotBlank
	@Pattern(regexp = "(?i)EMPLEADO", message = "El rol debe ser EMPLEADO")
	private String rol;

}
