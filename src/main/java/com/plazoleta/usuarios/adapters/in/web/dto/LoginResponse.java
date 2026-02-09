package com.plazoleta.usuarios.adapters.in.web.dto;

import com.plazoleta.usuarios.domain.model.Role;

public class LoginResponse {

	private Long id;
	private String correo;
	private Role rol;
	private String token;

	public LoginResponse(Long id, String correo, Role rol, String token) {
		this.id = id;
		this.correo = correo;
		this.rol = rol;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public String getCorreo() {
		return correo;
	}

	public Role getRol() {
		return rol;
	}

	public String getToken() {
		return token;
	}
}
