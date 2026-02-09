package com.plazoleta.usuarios.adapters.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class CreatePropietarioRequest {

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
