package com.plazoleta.usuarios.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateOwnerRequest {

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

	@NotNull
	private LocalDate fechaNacimiento;

	@NotBlank
	@Email
	private String correo;

	@NotBlank
	private String clave;

}
