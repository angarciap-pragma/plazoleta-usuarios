package com.plazoleta.usuarios.adapters.in.web.dto;

import com.plazoleta.usuarios.domain.model.Role;

public class UsuarioRoleResponse {

	private Long id;
	private Role rol;

	public UsuarioRoleResponse(Long id, Role rol) {
		this.id = id;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public Role getRol() {
		return rol;
	}
}
