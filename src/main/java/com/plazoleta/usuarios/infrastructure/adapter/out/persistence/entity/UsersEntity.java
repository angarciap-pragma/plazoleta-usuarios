package com.plazoleta.usuarios.infrastructure.adapter.out.persistence.entity;

import com.plazoleta.usuarios.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "usuarios")
public class UsersEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String apellido;

	@Column(nullable = false, unique = true)
	private String documentoIdentidad;

	@Column(nullable = false)
	private String celular;

	@Column(nullable = true)
	private LocalDate fechaNacimiento;

	@Column(nullable = false, unique = true)
	private String correo;

	@Column(nullable = false)
	private String clave;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role rol;

}
