package com.plazoleta.usuarios.adapters.out.persistence;

import com.plazoleta.usuarios.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String apellido;

	@Column(nullable = false)
	private String documentoIdentidad;

	@Column(nullable = false)
	private String celular;

	@Column(nullable = false)
	private LocalDate fechaNacimiento;

	@Column(nullable = false)
	private String correo;

	@Column(nullable = false)
	private String clave;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role rol;

	protected UsuarioEntity() {
	}

	public UsuarioEntity(
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
