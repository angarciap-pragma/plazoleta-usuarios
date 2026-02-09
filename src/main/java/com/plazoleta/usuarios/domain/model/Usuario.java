package com.plazoleta.usuarios.domain.model;

import java.time.LocalDate;

public class Usuario {

	private Long id;
	private String nombre;
	private String apellido;
	private String documentoIdentidad;
	private String celular;
	private LocalDate fechaNacimiento;
	private String correo;
	private String clave;
	private Role rol;

	public Usuario(
			Long id,
			String nombre,
			String apellido,
			String documentoIdentidad,
			String celular,
			LocalDate fechaNacimiento,
			String correo,
			String clave,
			Role rol) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.documentoIdentidad = documentoIdentidad;
		this.celular = celular;
		this.fechaNacimiento = fechaNacimiento;
		this.correo = correo;
		this.clave = clave;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public String getCelular() {
		return celular;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getCorreo() {
		return correo;
	}

	public String getClave() {
		return clave;
	}

	public Role getRol() {
		return rol;
	}
}
